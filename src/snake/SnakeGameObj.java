package snake;

import java.awt.*;
import java.util.LinkedList;

public abstract class SnakeGameObj {
    // Current position of the object
    private int px;
    private int py;

    // Size of object in Pixels
    private final int width;
    private final int height;

    // Velocity: number of pixels to move every time move() is called
    private int vx;
    private int vy;

    // Upper bounds of the area
    private final int maxX;
    private final int maxY;

    private LinkedList<Point> objs; // Coordinates on court

    public SnakeGameObj(
            int vx, int vy, int px, int py, int width, int height, int courtWidth,
            int courtHeight
    ) {
        this.vx = vx;
        this.vy = vy;
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;

        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;

        objs = new LinkedList<>();
        objs.addFirst(new Point(px, py));
    }

    public SnakeGameObj(
            int vx, int vy, int px, int py, int width, int height, int courtWidth,
            int courtHeight, LinkedList<Point> objs
    ) {
        this.vx = vx;
        this.vy = vy;
        this.px = px;
        this.py = py;
        this.width = width;
        this.height = height;

        this.maxX = courtWidth - width;
        this.maxY = courtHeight - height;

        this.objs = objs;

    }

    /**************************************************************************
     * GETTERS
     **************************************************************************/
    public int getPx() {
        return this.px;
    }

    public int getPy() {
        return this.py;
    }

    public int getVx() {
        return this.vx;
    }

    public int getVy() {
        return this.vy;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public LinkedList<Point> getObjs() {
        return objs;
    }

    /**************************************************************************
     * SETTERS
     **************************************************************************/
    public void setPx(int px) {
        this.px = px;
        clip();
    }

    public void setPy(int py) {
        this.py = py;
        clip();
    }

    public void setVx(int vx) {
        this.vx = vx;
    }

    public void setVy(int vy) {
        this.vy = vy;
    }

    public void setObjs(LinkedList<Point> bodies) {
        objs = bodies;
    }

    /**************************************************************************
     * UPDATES AND OTHER METHODS
     **************************************************************************/

    // Prevents the object from going outside the bounds of the area

    public void clip() {
        this.px = Math.min(Math.max(this.px, 0), this.maxX);
        this.py = Math.min(Math.max(this.py, 0), this.maxY);
    }

    // Moves the object by its velocity. Ensures that the object does not go outside its bounds by clipping
    public void move() {
        this.px += this.vx;
        this.py += this.vy;

        clip();
    }

    // Default draw method that provides how the object should be drawn in the GUI
    public void draw(Graphics g) {
    }

    // Checks if an object has left the boundaries of the frame
    public boolean hitWall() {
        return (this.px + this.vx < 0)
                || (this.px + this.vx > this.maxX)
                || (this.py + this.vy < 0)
                || (this.py + this.vy > this.maxY);
    }

    // Creates new object (for Apples and Lemons)
    public void generateNew() {
        objs.add(
                new Point(
                        ((int) (Math.random() * maxX)),
                        ((int) (Math.random() * maxY))
                )
        );
    }

}