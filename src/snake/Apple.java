package snake;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import javax.imageio.ImageIO;

public class Apple extends SnakeGameObj implements Fruits {
    // Randomized initial round position
    public static final int INIT_POS_X = (int) (Math.random() * 440);
    public static final int INIT_POS_Y = (int) (Math.random() * 440);

    private BufferedImage img;

    public static final String IMG_FILE = "files/apple.png";

    public Apple(int courtWidth, int courtHeight) {
        super(Fruits.INIT_VEL_X, Fruits.INIT_VEL_Y, INIT_POS_X, INIT_POS_Y, Fruits.SIZE, Fruits.SIZE, courtWidth, courtHeight);

        try {
            img = ImageIO.read(new File(IMG_FILE));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }

    }

    public Apple(
            int positionX, int positionY, int courtWidth,
            int courtHeight, LinkedList<Point> objs
    ) {
        super(
                Fruits.INIT_VEL_X, Fruits.INIT_VEL_Y, positionX, positionY,
                Fruits.SIZE, Fruits.SIZE, courtWidth, courtHeight, objs
        );
        try {
            img = ImageIO.read(new File(IMG_FILE));
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
    }

    @Override
    public void draw(Graphics g) {
        for (Point p : getObjs()) {
            g.drawImage(img, p.x, p.y, Fruits.SIZE, Fruits.SIZE, null);
        }
    }

    // Checks if the apple is eaten by snake and removes it from collection
    public boolean intersects(Snake that) {
        for (int i = 0; i < getObjs().size(); i++) {
            Point p = getObjs().get(i);
            if (p.x + getWidth() >= that.getPx() && p.y + getHeight() >= that.getPy()) {
                if (that.getPx() + that.getWidth() >= p.x
                        && that.getPy() + that.getHeight() >= p.y) {
                    LinkedList<Point> newList = getObjs();
                    newList.remove(i);
                    setObjs(newList);
                    return true;
                }
            }
        }
        return false;
    }

    // Specifies how eating an apple should change snake's behavior
    public void affectSnake(Snake snake) {
        snake.setVOfSnake(Math.abs(snake.getVOfSnake()));
        snake.growBigger(5);

    }

}