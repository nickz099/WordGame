package BookwormAdventures;


/*
 * CIS 120 HW09 - TicTacToe Demo
 * (c) University of Pennsylvania
 * Created by Bayley Tuch, Sabrina Green, and Nicolas Corona in Fall 2020.
 */


import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

/**
 * This class sets up the top-level frame and widgets for the GUI.
 *
 * This game adheres to a Model-View-Controller design framework. This
 * framework is very effective for turn-based games. We STRONGLY
 * recommend you review these lecture slides, starting at slide 8,
 * for more details on Model-View-Controller:
 * https://www.seas.upenn.edu/~cis120/current/files/slides/lec37.pdf
 *
 * In a Model-View-Controller framework, Game initializes the view,
 * implements a bit of controller functionality through the reset
 * button, and then instantiates a GameBoard. The GameBoard will
 * handle the rest of the game's view and controller functionality, and
 * it will instantiate a TicTacToe object to serve as the game's model.
 */
public class RunBookwormAdventures implements Runnable {
    public void run() {
        // NOTE: the 'final' keyword denotes immutability even for local variables.

        // Top-level frame in which game components live
        final JFrame frame = new JFrame("Word Battle");
        frame.setLocation(500, 300);
        Canvas gameScreen = new Canvas();
        Canvas artifacts = new Canvas();
        Canvas right = new Canvas();

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        frame.add(gameScreen, BorderLayout.CENTER);
        final JLabel status = new JLabel("Setting up...");
        status_panel.add(status);

        //button of letters
        JPanel letterGrid = new JPanel();
        letterGrid.setLayout(new GridLayout(4,4));

        //wordPanel
        JPanel wordPanel = new JPanel();
        wordPanel.setSize(50, 50);

        //atack button
        JButton attack = new JButton("ATTACK");
        attack.setSize(50, 50);

        JButton skip = new JButton ("SKIP TURN");
        skip.setSize(50, 50);


        // Game board
        final GameBoard board =
                new GameBoard(status, letterGrid, wordPanel,
                        attack, skip, Difficulty.NORMAL);
        //frame.add(board, BorderLayout.CENTER);
        //board is basically canvas

        //stuff that goes under
        JPanel letters = new JPanel();
        letters.setLayout(new GridLayout(0, 1));
        letters.add(wordPanel);
        JPanel attackAndSkip = new JPanel();
        attackAndSkip.add(attack);
        attackAndSkip.add(skip);
        letters.add(attackAndSkip);
        letters.add(letterGrid);

        JPanel bottom = new JPanel();
        bottom.add(artifacts);
        bottom.add(letters);
        bottom.add(right);


        //visual layout of the game
        JPanel gameLayout = new JPanel();
        gameLayout.setLayout(new GridLayout(0, 1));
        gameLayout.add(board);
        //gameLayout.add(wordPanel);
        gameLayout.add(bottom);
        frame.add(gameLayout, BorderLayout.CENTER);

        // Reset button
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);
        // Note here that when we add an action listener to the reset button, we
        // define it as an anonymous inner class that is an instance of
        // ActionListener with its actionPerformed() method overridden. When the
        // button is pressed, actionPerformed() will be called.
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> board.reset());
        control_panel.add(reset);

        final JButton normal = new JButton ("NORMAL");
        normal.addActionListener((ActionEvent e) -> {
            board.changeDifficulty(Difficulty.NORMAL);
        });

        final JButton hard = new JButton("HARD");
        hard.addActionListener((ActionEvent e) -> {
            board.changeDifficulty((Difficulty.HARD));
        });

        control_panel.add(normal);
        control_panel.add(hard);

        final JButton instruction = new JButton("Instructions");
        instruction.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String message =
                        "Welcome to Word Battle. In this thrilling adventure, you will fight \n" +
                         "various enemies using your wits. You are given a total of 16 letters \n" +
                        "that you must use to construct English words. The longer the word is, the more \n" +
                         "damage you will do to your enemy. When you click on a letter, it will disappear from the " +
                         "the matrix and appear above it. \n Clicking on a letter above the matrix will return it to\n"
                        + " matrix. After you've selected your word, click the Attack button. Your damage will \n" +
                         "be displayed. Then, the button will becom a \"Defend\" button, clickin it will process \n" +
                         "the enemy's turn in attacking you. \n" + "Some enemies have special attacks. The Spider \n" +
                         "can poison you, lowering your total damage output. The spider can poison you, lowering \n" +
                                " your overall damage output. The Siren has a chance to stun you,\n" +
                         "preventing you from doing anything for a turn. The vampire will heal itself for the \n" +
                        "amount of damage it does to you. The first vampire you defeat, you will be rewardded with \n" +
                         "the vampire ability heal back your damage. Beating half of the enemies will permanently \n"  +
                         "increase your damage output by 50%. Additionally, you can choose between normal and hard \n" +
                        "modes. Normal mode will choose letters based on how frequently they appear in the english \n" +
                        "language. Hard mode will choose letters at random. If you wish to skip your turn, you \n" +
                        "can hit the SKIP TURN button to do so. You win once you've beaten all the " +
                                "enemies and the boss. \n Have fun!";
                JOptionPane.showMessageDialog(null, message,
                        "Game Instructions", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        control_panel.add(instruction);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start the game
        board.reset();
    }
}
