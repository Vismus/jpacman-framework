package nl.tudelft.jpacman.level;

import nl.tudelft.jpacman.board.Board;
import nl.tudelft.jpacman.board.Direction;
import nl.tudelft.jpacman.board.Square;
import nl.tudelft.jpacman.board.Unit;
import nl.tudelft.jpacman.level.task.ExitHuntingModeTask;
import nl.tudelft.jpacman.level.task.GhostMoveTask;
import nl.tudelft.jpacman.level.task.PlayerMoveTask;
import nl.tudelft.jpacman.level.task.ScheduledTaskService;
import nl.tudelft.jpacman.level.unit.Pellet;
import nl.tudelft.jpacman.level.unit.Player;
import nl.tudelft.jpacman.npc.Ghost;

import java.util.*;

/**
 * A level of Pac-Man. A level consists of the board with the players and the
 * AIs on it.
 *
 * @author Jeroen Roosen
 */
@SuppressWarnings("PMD.TooManyMethods")
public class Level {

    /**
     * The board of this level.
     */
    private final Board board;

    /**
     * The lock that ensures moves are executed sequential.
     */
    private final Object moveLock = new Object();

    /**
     * The lock that ensures starting and stopping can't interfere with each
     * other.
     */
    private final Object startStopLock = new Object();

    /**
     * The NPCs of this level their moving schedules.
     */
    private final Map<Ghost, ScheduledTaskService> npcsMoveSchedules;

    /**
     * The NPCs of this level their reborn schedules.
     */
    private final Map<Ghost, ScheduledTaskService> npcsRebornSchedules;

    /**
     * The players on this level and, if they are running, their schedules.
     */
    private final Map<Player, ScheduledTaskService> playersMoveSchedules;

    /**
     * The ScheduledTaskService allowing to gradually leave the hunting mode.
     */
    private ScheduledTaskService exitHuntingModeService;

    /**
     * <code>true</code> iff this level is currently in progress, i.e. players
     * and NPCs can move.
     */
    private boolean inProgress;

    /**
     * The squares from which players can start this game.
     */
    private final List<Square> startSquares;

    /**
     * The squares in which a fruit can be created during the game.
     */
    private final List<Square> fruitSquares;

    /**
     * the spawn fruit, false when no fruit is placed during the game, otherwise it is true.
     */
    private boolean spawnFruit = false;

    /**
     * The initial number of pellets before starting the game
     */
    private final int nbPellets;

    /**
     * The start current selected starting square.
     */
    private int startSquareIndex;

    /**
     * The table of possible collisions between units.
     */
    private final CollisionMap collisions;

    /**
     * The objects observing this level.
     */
    private final Set<LevelObserver> observers;

    /**
     * The factory providing the fruits.
     */
    private final FruitFactory fruitFactory;

    /**
     * The current game mode of the level.
     * 0: ghosts chase pacman
     * 1: pacman chase vulnerable ghosts
     * 2: pacman chase vulnerable ghosts but it will end soon.
     */
    private byte gameMode;

    /**
     * Creates a new level for the board.
     *
     * @param board          The board for the level.
     * @param ghosts         The ghosts on the board.
     * @param startPositions The squares on which players start on this board.
     * @param fruitPositions The squares on which a fruit can be created on the board
     */
    public Level(Board board, List<Ghost> ghosts, List<Square> startPositions, List<Square> fruitPositions, FruitFactory fruitFactory) {
        assert board != null;
        assert ghosts != null;
        assert startPositions != null;
        assert fruitPositions != null;

        this.board = board;
        this.nbPellets = remainingPellets(false);
        this.inProgress = false;
        this.npcsMoveSchedules = new HashMap<>();
        this.npcsRebornSchedules = new HashMap<>();
        for (Ghost ghost : ghosts) {
            ScheduledTaskService moveService = new ScheduledTaskService();
            moveService.schedule(new GhostMoveTask(moveService, ghost, this), ghost.getInterval() / 2, false);
            npcsMoveSchedules.put(ghost, moveService);

            npcsRebornSchedules.put(ghost, new ScheduledTaskService());
        }
        this.playersMoveSchedules = new HashMap<>();
        this.startSquares = startPositions;
        this.startSquareIndex = 0;
        this.collisions = new DefaultPlayerInteractionMap(this);
        this.observers = new HashSet<>();
        this.exitHuntingModeService = new ScheduledTaskService();
        this.fruitSquares = fruitPositions;
        this.fruitFactory = fruitFactory;
    }

    /**
     * Adds an observer that will be notified when the level is won or lost.
     *
     * @param observer The observer that will be notified.
     */
    public void addObserver(LevelObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer if it was listed.
     *
     * @param observer The observer to be removed.
     */
    public void removeObserver(LevelObserver observer) {
        observers.remove(observer);
    }

    /**
     * Accessor of the gamemode of this level
     *
     * @return the game mode
     * 0: ghosts chase pacman
     * 1: pacman chase vulnerable ghosts
     * 2: pacman chase vulnerable ghosts but it will end soon.
     */
    public byte getGameMode() {
        return gameMode;
    }

    /**
     * Set the game mode of this level
     *
     * @param newGameMode 0: ghosts chase pacman
     *                    1: pacman chase vulnerable ghosts
     *                    2: pacman chase vulnerable ghosts but it will end soon.
     */
    public void setGameMode(byte newGameMode) {
        if (newGameMode == 0) {
            stopHuntingMode();
        } else if (newGameMode == 1) {
            stopHuntingMode();
            startHuntingMode();
        }
        this.gameMode = newGameMode;
    }

    /**
     * Stop the task of gradual exit from hunting mode.
     */
    private void stopHuntingMode() {
        this.exitHuntingModeService.cancelTask();
        for (Player player : this.getPlayers()) {
            player.setConsecutiveKills(0);
        }
    }

    /**
     * Start the task of gradual exit from hunting mode.
     * If there are 2 power pellets or less, the hunting mode time is reduced by 2 seconds.
     */
    private void startHuntingMode() {
        if (this.remainingPellets(true) > 2) {
            this.exitHuntingModeService.schedule(new ExitHuntingModeTask(this.exitHuntingModeService, this), 7000, true);
        } else {
            this.exitHuntingModeService.schedule(new ExitHuntingModeTask(this.exitHuntingModeService, this), 5000, true);
        }
        for (Ghost npc : this.getGhosts()) {
            npc.setAlive(true);
            npc.setGameMode((byte) 1);
        }
    }

    /**
     * Registers a player on this level, assigning him to a starting position. A
     * player can only be registered once, registering a player again will have
     * no effect.
     *
     * @param player The player to register.
     */
    public void registerPlayer(Player player) {
        assert player != null;
        assert !startSquares.isEmpty();

        if (playersMoveSchedules.containsKey(player)) {
            return;
        }
        ScheduledTaskService service = new ScheduledTaskService();
        service.schedule(new PlayerMoveTask(service, player, this), player.getInterval() / 2, false);
        playersMoveSchedules.put(player, service);
        Square square = startSquares.get(startSquareIndex);
        player.occupy(square);
        startSquareIndex++;
        startSquareIndex %= startSquares.size();
    }

    /**
     * Returns the board of this level.
     *
     * @return The board of this level.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Returns the number of pellets before running the level.
     *
     * @return The number of pellets into the board.
     */
    public int getNbPellets() {
        return nbPellets;
    }

    /**
     * Moves the unit into the given direction if possible and handles all
     * collisions.
     *
     * @param unit      The unit to move.
     * @param direction The direction to move the unit in.
     */
    public void move(Unit unit, Direction direction) {
        if (!isInProgress()) {
            return;
        }

        synchronized (moveLock) {
            unit.setDirection(direction);
            Square location = unit.getSquare();
            Square destination = location.getSquareAt(direction);

            if (destination.isAccessibleTo(unit)) {
                List<Unit> occupants = destination.getOccupants();
                unit.occupy(destination);
                for (Unit occupant : occupants) {
                    collisions.collide(unit, occupant);
                }
            }
            updateObservers();
        }
    }

    /**
     * Starts or resumes this level. The players and the ghosts start moving.
     */
    public void start() {
        synchronized (startStopLock) {
            if (isInProgress()) {
                return;
            }
            startNPCs();
            startPlayers();
            this.exitHuntingModeService.resume();
            inProgress = true;
            updateObservers();
        }
    }

    /**
     * Stops or pauses this level, no longer allowing any movement on the board.
     */
    public void stop() {
        synchronized (startStopLock) {
            if (!isInProgress()) {
                return;
            }
            stopNPCs();
            stopPlayers();
            this.exitHuntingModeService.suspend();
            inProgress = false;
        }
    }

    /**
     * Starts all NPC scheduling.
     */
    private void startNPCs() {
        this.npcsMoveSchedules.values().forEach(ScheduledTaskService::resume);
        this.npcsRebornSchedules.values().forEach(ScheduledTaskService::resume);
    }

    /**
     * Stops all NPC scheduling
     */
    private void stopNPCs() {
        this.npcsMoveSchedules.values().forEach(ScheduledTaskService::suspend);
        this.npcsRebornSchedules.values().forEach(ScheduledTaskService::suspend);
    }

    /**
     * Starts all Players movement scheduling.
     */
    private void startPlayers() {
        this.playersMoveSchedules.values().forEach(ScheduledTaskService::resume);
    }

    /**
     * Stops all Players movement scheduling.
     */
    private void stopPlayers() {
        this.playersMoveSchedules.values().forEach(ScheduledTaskService::suspend);
    }

    /**
     * Accessor of all the ghosts of the level.
     *
     * @return A set of ghosts
     */
    public Set<Ghost> getGhosts() {
        return npcsMoveSchedules.keySet();
    }

    /**
     * Accessor of all the players of the level.
     *
     * @return A set of players
     */
    public Set<Player> getPlayers() {
        return playersMoveSchedules.keySet();
    }

    /**
     * Defines the rebirth of a ghost after a certain delay.
     *
     * @param ghost The ghost to be reborn.
     * @param delay The delay
     */
    public void scheduleReborn(Ghost ghost, long delay) {
        this.npcsRebornSchedules.get(ghost).schedule(() -> ghost.setAlive(true), delay, true);
    }

    /**
     * Returns whether this level is in progress, i.e. whether moves can be made
     * on the board.
     *
     * @return <code>true</code> iff this level is in progress.
     */
    public boolean isInProgress() {
        return inProgress;
    }

    /**
     * Updates the observers about the state of this level.
     */
    private void updateObservers() {
        if (!isAnyPlayerAlive()) {
            for (LevelObserver observer : observers) {
                observer.levelLost();
            }
        }
        if (remainingPellets(false) == 0) {
            for (LevelObserver observer : observers) {
                observer.levelWon();
            }
        }

        if (remainingPellets(false) == nbPellets / 2 && !spawnFruit) {
            for (LevelObserver observer : observers) {
                observer.halfPelletsEaten();
            }
        }
    }

    /**
     * Returns <code>true</code> iff at least one of the players in this level
     * is alive.
     *
     * @return <code>true</code> if at least one of the registered players is
     * alive.
     */
    public boolean isAnyPlayerAlive() {
        for (Player player : playersMoveSchedules.keySet()) {
            if (player.isAlive()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Counts the pellets remaining on the board.
     *
     * @param powerPellet True to count the number of remaining power pellets false for simple pellets
     * @return The amount of pellets remaining on the board.
     */
    public int remainingPellets(boolean powerPellet) {
        Board board = getBoard();
        int pellets = 0;
        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                for (Unit unit : board.squareAt(x, y).getOccupants()) {
                    if (unit instanceof Pellet) {
                        if (((Pellet) unit).isPowerPellet() == powerPellet) {
                            pellets++;
                        }
                    }
                }
            }
        }
        return pellets;
    }

    public void placeFruit() {
        int index = this.fruitSquares.size() == 1 ? 0 : new Random().nextInt(this.fruitSquares.size());
        this.fruitFactory.createFruit().occupy(this.fruitSquares.get(index));
    }

    /**
     * An observer that will be notified when the level is won or lost.
     *
     * @author Jeroen Roosen
     */
    public interface LevelObserver {

        /**
         * The level has been won. Typically the level should be stopped when
         * this event is received.
         */
        void levelWon();

        /**
         * The level has been lost. Typically the level should be stopped when
         * this event is received.
         */
        void levelLost();

        /**
         * When pacman eats the half of pellets in the level, then the level should
         * be placed a fruit in the board.
         */
        void halfPelletsEaten();
    }
}
