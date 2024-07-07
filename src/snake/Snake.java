package snake;

import java.awt.*;
import java.util.LinkedList;

public class Snake extends SnakeGameObj {
    // Constants when creating a snake
    public static final int SIZE = 25;
    public static final int INIT_POS_X = 250;
    public static final int INIT_POS_Y = 250;
    public static final int INIT_VEL_X = 0;
    public static final int INIT_VEL_Y = 0;

    private int vOfSnake = 4;

    private int pxSnake = getPx();
    private int pySnake = getPy();

    private final Color color;

    public Snake(int courtWidth, int courtHeight, Color color) {
        super(
                INIT_VEL_X, INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth,
                courtHeight
        );
        this.color = color;
    }

    public Snake(
            int positionX, int positionY, int boardWidth,
            int boardHeight, LinkedList<Point> objs, Color color
    ) {
        super(
                INIT_VEL_X, INIT_VEL_Y, positionX, positionY, SIZE, SIZE, boardWidth, boardHeight,
                objs
        );
        this.color = color;
    }

    public int getVOfSnake() {
        return vOfSnake;
    }

    public void setVOfSnake(int v) {
        vOfSnake = v;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.color);
        for (Point p : getObjs()) {
            g.fillRect(p.x, p.y, SIZE, SIZE);
        }
    }

    // Loops through body to update position and updates head position
    @Override
    public void move() {
        for (int i = getObjs().size() - 1; i >= 1; i--) {
            getObjs().get(i).setLocation(getObjs().get(i - 1));
        }

        setPx(pxSnake + getVx());
        setPy(pySnake + getVy());

        pxSnake = getPx();
        pySnake = getPy();

        getObjs().set(0, new Point(pxSnake, pySnake));

        clip();
    }

    // Called when apple or lemon intersects with snake to make snake longer
    public void growBigger(int size) {
        for (int i = 0; i < size; i++) {
            LinkedList<Point> newBody;
            newBody = getObjs();
            newBody.add(new Point(getObjs().getLast()));
            setObjs(newBody);
        }
    }

    // Checks if the snake runs into its own body
    public boolean hitBody() {
        if (getObjs().size() > 1) {
            for (int i = 1; i < getObjs().size(); i++) {
                Point curr = getObjs().getFirst();
                if ((curr.x + getVx() == getObjs().get(i).x)) {
                    if (curr.y + getVy() == getObjs().get(i).y) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}