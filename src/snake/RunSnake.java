package snake;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class RunSnake implements Runnable {
    private int highScore;

    public void run() {

        // Top-level frame in which game components live.
        final JFrame frame = new JFrame("WELCOME TO SNAKE");
        frame.setLocation(250, 250);

        // Checks for high score
        BufferedReader scoreReader = null;
        try {
            scoreReader = new BufferedReader(new FileReader("files/highScore.txt"));
        } catch (FileNotFoundException f) {
            System.out.println("Internal Error:" + f.getMessage());
        }

        try {
            if (scoreReader != null) {
                highScore = Integer.parseInt(scoreReader.readLine().trim());
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        // Status panel
        final JPanel status_panel = new JPanel();
        frame.add(status_panel, BorderLayout.SOUTH);
        final JLabel status = new JLabel("CURRENT SCORE: 0     BEST SCORE: " + highScore);
        status_panel.add(status);

        // Main playing area
        final SnakeGameCourt court = new SnakeGameCourt(status);
        frame.add(court, BorderLayout.CENTER);

        // Control panel
        final JPanel control_panel = new JPanel();
        frame.add(control_panel, BorderLayout.NORTH);

        // Instructions button
        final JButton instructions = new JButton("Instructions");
        instructions.addActionListener(e -> court.instructions());
        control_panel.add(instructions);

        // Reset button
        final JButton reset = new JButton("Reset");
        reset.addActionListener(e -> court.reset());
        control_panel.add(reset);

        // Pause button
        final JButton pause = new JButton("Pause");
        pause.addActionListener(e -> court.pause());
        control_panel.add(pause);

        // Resume button
        final JButton resume = new JButton("Resume");
        resume.addActionListener(e -> court.resume());
        control_panel.add(resume);

        // Put the frame on the screen
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        // Start game by resetting and always display instructions when starting
        court.reset();
        court.instructionsDisplayed = true;
    }
}
