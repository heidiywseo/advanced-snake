package snake;

import snake.Snake;

public interface Fruits {
    // Constants for all fruits
    int SIZE = 25;
    int INIT_VEL_X = 0;
    int INIT_VEL_Y = 0;

    // Methods that all fruits should have
    boolean intersects(Snake that);

    void affectSnake(Snake snake);
}