package BookwormAdventures;

/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.List;
import java.util.LinkedList;

/**
 * This class instantiates a TicTacToe object, which is the model for the game.
 * As the user clicks the game board, the model is updated. Whenever the model
 * is updated, the game board repaints itself and updates its status JLabel to
 * reflect the current state of the model.
 *
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 *
 * In a Model-View-Controller framework, GameBoard stores the model as a field
 * and acts as both the controller (with a MouseListener) and the view (with
 * its paintComponent method and the status JLabel).
 */
@SuppressWarnings("serial")
public class GameBoard extends JPanel {

    private BookwormAdventures bw; // model for the game
    private JLabel status; // current status text

    // Game constants
    public static final int BOARD_WIDTH = 300;
    public static final int BOARD_HEIGHT = 300;

    //labels
    private JLabel playerHealth;
    private JLabel enemyHealth;
    private JLabel score;

    //Game stuff
    private JButton[][] letterGrid;
    private List<JButton> word = new LinkedList<>();
    private JPanel wordPanel;
    private JPanel buttons;
    private final int INTERVAL = 35;
    private JButton attack;


    //saved info
    private boolean gameEnded = false;

    /**
     * Initializes the game board.
     */
    public GameBoard(JLabel statusInit, JPanel buttons, JPanel wordPanel, JButton attack, JButton skip,
                     Difficulty difficulty) {
        // creates border around the court area, JComponent method
        setBorder(BorderFactory.createLineBorder(Color.BLACK));

        // Enable keyboard focus on the court area. When this component has the
        // keyboard focus, key events are handled by its key listener.
        setFocusable(true);

        bw = new BookwormAdventures(difficulty); // initializes model for the game
        status = statusInit; // initializes the status JLabel
        this.buttons = buttons;
        this.wordPanel = wordPanel;
        letterGrid = new JButton[4][4];

        attack.addActionListener((ActionEvent i) ->  {
            bw.playTurn();
            //need to find a way to update the array for every playturn
            initializeGrid();
            updateStatus();
            repaint();
            updatePanel();
            word.clear();
            updatePanel();
        });

        skip.addActionListener((ActionEvent e) -> {
            bw.skipTurn();
            if (bw.getCurrentPlayer()) {
                bw.playTurn();
            }
            initializeGrid();
            updateStatus();
            repaint();
            updatePanel();
            word.clear();
            updatePanel();
        });
        this.attack = attack;
    }

    /**
     * (Re-)sets the game to its initial state.
     */
    public void reset() {
        bw.reset();
        word.clear();
        wordPanel.removeAll();
        buttons.removeAll();
        initializeGrid();
        status.setText("Player 1's Turn");
        updateStatus();
        repaint();
        // Makes sure this component has keyboard/mouse focus
        updatePanel();
        requestFocusInWindow();
    }

    //make an array of Letter objects
    //make a second array of button objects from Letter
    //add actionListeners to each button to put them into a list, and remove from array
    //when a button is added to list, do the same for the Letter object
    //if clicked in a list, the button returns to its former place in the array, as does the letter object
    public void initializeGrid() {
        //remember to intializeGrid every playTurn it's the player's turn!!! 
        Letter[][] letters = bw.getLetterArray();
        for (int row = 0; row < letters.length; row++) {
            for (Letter l: letters[row]) {
                if (l != null) {
                    JButton b = l.getButton();
                    b.addActionListener((ActionEvent e) -> {
                        bw.transfer(l);
                        if (l.getIsInArray() && word.contains(b)) {
                            //letters[l.getRow()][l.getCol()] = l;
                            word.remove(b);
                            letterGrid[l.getRow()][l.getCol()] = b;
                            repaint();
                            updatePanel();
                            System.out.println("Removed " + l.getLETTER() + " from word");
                        } else {
                            //transfer out of array
                            //theoretically, letter should switch booleans, so don't need to update here?
                            word.add(b);
                            letterGrid[l.getRow()][l.getCol()] = new JButton("");
                            updatePanel();
                            repaint();
                            System.out.println("Added " + l.getLETTER() + " to word");
                            repaint();
                            updatePanel();
                        }
                    });
                    if (l.getIsInArray()) {
                        letterGrid[l.getRow()][l.getCol()] = b;
                    } else {
                        letterGrid[l.getRow()][l.getCol()] = new JButton("");
                    }
                } else {
                    throw new IllegalArgumentException("The Letter Object is null and shouldn't be");
                }
            }
        }
    }

    public void changeDifficulty(Difficulty d) {
        bw = new BookwormAdventures(d);
        reset();
    }



    /**
     * Updates the JLabel to reflect the current state of the game.
     */
    private void updateStatus() {
        //change this condition soon
        if (bw.getCurrentPlayer()) {
            status.setText("Your turn!");
        } else {
            status.setText("Brace!");
        }

        int winner = bw.checkWinner();
        if (winner == 1) {
            status.setText("You've won! Onto the next one!");
        } else if (winner == 2) {
            status.setText("NOOOOOOO! You've Lost!");
        } else if (winner == 0) {
            status.setText("The fight continues...");
        } else if (winner == 3) {
            status.setText("You've beaten the game!");
        }
    }

    private void updatePanel() {
            //add whatever elements are in the collections into the panels
            wordPanel.removeAll();
            buttons.removeAll();
            for (JButton b: word) {
                wordPanel.add(b);
            }
            for (int row = 0; row < letterGrid.length; row++) {
                for (int col = 0; col < letterGrid[row].length; col++) {
                    buttons.add(letterGrid[row][col]);
                }
            }
            if (bw.getCurrentPlayer()) {
                attack.setText("ATTACK");
            } else {
                attack.setText("DEFEND");
            }
        revalidate();
    }
    /**
     * Draws the game board.
     *
     * There are many ways to draw a game board. This approach
     * will not be sufficient for most games, because it is not
     * modular. All of the logic for drawing the game board is
     * in this method, and it does not take advantage of helper
     * methods. Consider breaking up your paintComponent logic
     * into multiple methods or classes, like Mushroom of Doom.
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        //draw combat screen
        //north border
        g.drawLine(100, 100, 1300, 100);
        //right border
        g.drawLine(1300, 100, 1300, 500);
        //south border
        //g.drawLine(1300, 500, 100, 500);
        //left border
        g.drawLine(100, 500, 100, 100);


        //draw player health bar
        bw.getPlayer().drawHealthBar(g);
        bw.getPlayer().draw(g);

        //draw enemy health bar
        bw.getEnemy().drawHealthBar(g);
        //draw enemy
        bw.getEnemy().draw(g);

        if (bw.getGameOver() && bw.getPlayerWon()) {
            g.setColor(Color.BLACK);
            g.fillRect(500, 200, 500, 100);
            g.setColor(Color.WHITE);
            g.drawString("YOU'VE WON", 700, 250);
        }
    }

    /**
     * Returns the size of the game board.
     */
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    }
}
