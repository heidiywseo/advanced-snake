package snake;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;

public class SnakeGameCourt extends JPanel {

    private Apple apple;

    private Lemon lemon;

    private Snake snake;

    private boolean playing = false;

    private final JLabel status;

    private int score = 0;
    private int highScore = 0;

    private final Timer timer;

    // Game constants
    public static final int COURT_WIDTH = 500;
    public static final int COURT_HEIGHT = 500;

    private static final int TIMER_INTERVAL = 30;

    Color snakeColor = new Color(150, 26, 217);

    private static BufferedImage instructionPage;

    boolean instructionsDisplayed = true;

    // Initialize the Game Board
    public SnakeGameCourt(JLabel status) {

        setBorder(BorderFactory.createLineBorder(Color.BLACK));
        setBackground(new Color(79, 219, 116));

        // Reads for high score
        BufferedReader highScoreReader = null;
        try {
            highScoreReader = new BufferedReader(new FileReader("files/highScore.txt"));
            try {
                highScore = Integer.parseInt(highScoreReader.readLine().trim());
            } catch (IOException e) {
                System.out.println("Internal Error:" + e.getMessage());
            }
        } catch (FileNotFoundException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        // Reads to display instructions image
        try {
            instructionPage = ImageIO.read(new File("files/instructions.png"));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        // Timer
        timer = new Timer(TIMER_INTERVAL, e -> tick());
        timer.start();

        // Keyboard focus on the court area
        setFocusable(true);

        // Key listener to move snake with arrow keys
        addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                    snake.setVx(-snake.getVOfSnake());
                    snake.setVy(0);
                }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    snake.setVx(snake.getVOfSnake());
                    snake.setVy(0);
                }
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    snake.setVx(0);
                    snake.setVy(-snake.getVOfSnake());
                }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    snake.setVx(0);
                    snake.setVy(snake.getVOfSnake());
                }
            }
        });
        this.status = status;
    }

    // Reset to initial state
    public void reset() {

        snake = new Snake(500, 500, new Color(150, 26, 217));
        apple = new Apple(500, 500);
        lemon = new Lemon(500, 500);

        snake.setVOfSnake(4);

        instructionsDisplayed = false;

        score = 0;

        playing = true;
        status.setText("CURRENT SCORE: " + score + "     HIGH SCORE: " + highScore);

        if (!timer.isRunning()) {
            timer.start();
        }

        repaint();

        requestFocusInWindow();
    }

    // Called when timer defined
    public void tick() {

        boolean check = false;
        if (playing) {
            snake.move();

            // Checks if apple or lemon is eaten and responds accordingly
            if (apple.intersects(snake)) {
                apple.affectSnake(snake);
                score += 1;
                status.setText("CURRENT SCORE: " + score + "     HIGH SCORE: " + highScore);
                check = true;
            }

            if (lemon.intersects(snake)) {
                lemon.affectSnake(snake);
                score += 1;
                status.setText("CURRENT SCORE: " + score + "     HIGH SCORE: " + highScore);
                check = true;
            }

            // Randomly generates new apple of lemon (more apples than lemons)
            if (check) {
                if (Math.random() <= 0.7) {
                    apple.generateNew();
                } else {
                    lemon.generateNew();
                }
                check = false;
            }

            // Updates high score as game is being played
            if (score > highScore) {
                highScore = score;
                try {
                    BufferedWriter highScoreWriter = new BufferedWriter(
                            new FileWriter("files/highScore.txt")
                    );
                    highScoreWriter.write(Integer.toString(score));
                    highScoreWriter.close();
                } catch (IOException e) {
                    System.out.println("Internal Error:" + e.getMessage());
                }

                status.setText("CURRENT SCORE: " + score + "     HIGH SCORE: " + highScore);

            }

            // Stops game if the snake hits the wall or itself
            if (snake.hitWall() || snake.hitBody()) {
                playing = false;
                status.setText("You died!");
            }
            repaint();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (instructionsDisplayed) {
            g.drawImage(instructionPage, 50, 50, 400, 400, null);
        } else {
            snake.draw(g);
            apple.draw(g);
            lemon.draw(g);
        }
    }

    // Size of game board
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(COURT_WIDTH, COURT_HEIGHT);
    }

    // Stop game, able to resume
    public void pause() {
        try {
            File saved = new File("files/savedGameState.txt");
            FileWriter save = new FileWriter(saved);
            BufferedWriter gameStateWriter = new BufferedWriter(save);

            gameStateWriter.write(snake.getPx() + "," + snake.getPy());
            gameStateWriter.newLine();

            gameStateWriter.write(snake.getVx() + "," + snake.getVy());
            gameStateWriter.newLine();

            gameStateWriter.write(snake.getVOfSnake() + "," + " ");
            gameStateWriter.newLine();

            gameStateWriter.write(apple.getPx() + "," + apple.getPy());
            gameStateWriter.newLine();

            gameStateWriter.write(lemon.getPx() + "," + lemon.getPy());
            gameStateWriter.newLine();

            gameStateWriter.write(Integer.toString(score));
            gameStateWriter.close();

        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        try {
            File snakeFile = new File("files/snakeObjs.txt");
            FileWriter snakeFileWriter = new FileWriter(snakeFile);
            BufferedWriter snakeWriter = new BufferedWriter(snakeFileWriter);

            for (int i = 0; i < snake.getObjs().size(); i++) {
                snakeWriter.write(
                        Integer.toString(snake.getObjs().get(i).x) + ","
                                + Integer.toString(snake.getObjs().get(i).y)
                );
                snakeWriter.newLine();
            }
            snakeWriter.close();
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        try {
            File appleFile = new File("files/apples.txt");
            FileWriter appleFileWriter = new FileWriter(appleFile);
            BufferedWriter appleWriter = new BufferedWriter(appleFileWriter);

            for (int i = 0; i < apple.getObjs().size(); i++) {
                appleWriter.write(
                        Integer.toString(apple.getObjs().get(i).x) + ","
                                + Integer.toString(apple.getObjs().get(i).y)
                );
                appleWriter.newLine();
            }
            appleWriter.close();
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        try {
            File lemonFile = new File("files/lemons.txt");
            FileWriter lemonFileWriter = new FileWriter(lemonFile);
            BufferedWriter lemonWriter = new BufferedWriter(lemonFileWriter);

            for (int i = 0; i < lemon.getObjs().size(); i++) {
                lemonWriter.write(
                        Integer.toString(lemon.getObjs().get(i).x) + ","
                                + Integer.toString(lemon.getObjs().get(i).y)
                );
            }
            lemonWriter.close();
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

        timer.stop();
        playing = false;

        repaint();
        requestFocusInWindow();
    }

    // Resumes game by reading from files
    public void resume() {

        int pxSnake = 0;
        int pySnake = 0;

        int snakeVx = 0;
        int snakeVy = 0;

        int snakeV = 0;

        int pxApple = 0;
        int pyApple = 0;

        int pxLemon = 0;
        int pyLemon = 0;

        int currScore = 0;

        try {
            FileReader save = new FileReader("files/savedGameState.txt");
            BufferedReader gameStateReader = new BufferedReader(save);

            String snakePosition = null;
            String snakeVxy = null;
            String snakeVGen = null;
            String applePosition = null;
            String lemonPosition = null;
            String scoreFile = null;

            try {
                try {
                    snakePosition = gameStateReader.readLine().trim();
                    snakeVxy = gameStateReader.readLine().trim();
                    snakeVGen = gameStateReader.readLine().trim();
                    applePosition = gameStateReader.readLine().trim();
                    lemonPosition = gameStateReader.readLine().trim();
                    scoreFile = gameStateReader.readLine().trim();

                } catch (NullPointerException n) {
                    reset();
                }

            } catch (IOException e) {
                System.out.println("Internal Error:" + e.getMessage());
            }

            if (snakePosition != null) {
                String[] snakePositions = snakePosition.split(",");
                pxSnake = Integer.parseInt(snakePositions[0]);
                pySnake = Integer.parseInt(snakePositions[1]);
            }

            if (snakeVxy != null) {
                String[] snakeVs = snakeVxy.split(",");
                snakeVx = Integer.parseInt(snakeVs[0]);
                snakeVy = Integer.parseInt(snakeVs[1]);
            }

            if (snakeVGen != null) {
                String[] snakeVGens = snakeVGen.split(",");
                snakeV = Integer.parseInt(snakeVGens[0]);
            }

            if (applePosition != null) {
                String[] applePositions = applePosition.split(",");
                pxApple = Integer.parseInt(applePositions[0]);
                pyApple = Integer.parseInt(applePositions[1]);
            }

            if (lemonPosition != null) {
                String[] lemonPositions = lemonPosition.split(",");
                pxLemon = Integer.parseInt(lemonPositions[0]);
                pyLemon = Integer.parseInt(lemonPositions[1]);
            }

            if (scoreFile != null) {
                currScore = Integer.parseInt(scoreFile);
            }

        } catch (IOException e) {
            reset();
            System.out.println("Internal Error:" + e.getMessage());
        }

        FileLineIterator snakeIterator = new FileLineIterator("files/snakeObjs.txt");
        LinkedList<Point> snakeObjs = new LinkedList<>();
        String[] snakePos;
        while (snakeIterator.hasNext()) {
            snakePos = snakeIterator.next().split(",");
            snakeObjs.add(new Point(Integer.parseInt(snakePos[0]), Integer.parseInt(snakePos[1])));
        }

        FileLineIterator appleIterator = new FileLineIterator("files/apples.txt");
        LinkedList<Point> appleObjs = new LinkedList<>();
        String[] applePos;
        while (appleIterator.hasNext()) {
            applePos = appleIterator.next().split(",");
            appleObjs.add(new Point(Integer.parseInt(applePos[0]), Integer.parseInt(applePos[1])));
        }

        FileLineIterator lemonIterator = new FileLineIterator("files/lemons.txt");
        LinkedList<Point> lemonObjs = new LinkedList<>();
        String[] lemonPos;
        while (lemonIterator.hasNext()) {
            lemonPos = lemonIterator.next().split(",");
            lemonObjs.add(new Point(Integer.parseInt(lemonPos[0]), Integer.parseInt(lemonPos[1])));
        }

        snake = new Snake(pxSnake, pySnake, COURT_WIDTH, COURT_HEIGHT, snakeObjs, snakeColor);
        apple = new Apple(pxApple, pyApple, COURT_WIDTH, COURT_HEIGHT, appleObjs);
        lemon = new Lemon(pxLemon, pyLemon, COURT_WIDTH, COURT_HEIGHT, lemonObjs);

        snake.setVx(snakeVx);
        snake.setVy(snakeVy);

        snake.setVOfSnake(snakeV);

        score = currScore;

        status.setText("CURRENT SCORE: " + score + "     HIGH SCORE: " + highScore);

        playing = true;
        timer.start();

        instructionsDisplayed = false;

        repaint();
        requestFocusInWindow();
    }

    // Correctly displays instructions when method called from button pushed
    public void instructions() {
        instructionsDisplayed = true;
        playing = false;
        pause();
        repaint();
        requestFocusInWindow();
    }

}