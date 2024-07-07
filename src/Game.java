import javax.swing.*;

public class Game {
    // Starts and runs the game. Initializes the runnable game class
    public static void main(String[] args) {
        Runnable game = new snake.RunSnake();

        SwingUtilities.invokeLater(game);
    }
}
