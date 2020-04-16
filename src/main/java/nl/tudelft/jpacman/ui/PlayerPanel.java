package nl.tudelft.jpacman.ui;

import nl.tudelft.jpacman.level.unit.Player;

import javax.swing.*;
import java.awt.*;

public class PlayerPanel extends JPanel {
    /**
     * Default serialisation ID.
     */
    private static final long serialVersionUID = 1L;

    private final JLabel scoreLabel;
    private final JLabel remainingLifeLabel;

    public PlayerPanel(Player player) {
        super();
        assert player != null;

        setLayout(new GridLayout(2, 1));

        scoreLabel = new JLabel("0", JLabel.CENTER);
        remainingLifeLabel = new JLabel("3", JLabel.CENTER);
        add(scoreLabel);
        add(remainingLifeLabel);
    }

    public void refresh(String score, int remainingLifes) {
        scoreLabel.setText(score);
        remainingLifeLabel.setText(String.format("Lifes: %1d", remainingLifes));
    }
}
