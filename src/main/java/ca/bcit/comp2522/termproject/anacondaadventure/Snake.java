package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class Snake {
    private List<Point> body;
    private Direction direction;
    private static final Color SNAKE_COLOR = Color.GREEN;

    public Snake() {
        body = new ArrayList<>();
        body.add(new Point(5, 5));
        body.add(new Point(5, 6));
        body.add(new Point(5, 7));
        direction = Direction.RIGHT;
    }

    public void init() {

    }

    public void update() {
        move();

    }

    private void move() {

        Point head = body.get(0);
        Point newHead = new Point(head.getX() + direction.getX(), head.getY() + direction.getY());
        body.add(0, newHead);
        body.remove(body.size() - 1);
    }

    public void changeDirection(Direction newDirection) {

        if (!direction.isOpposite(newDirection)) {
            this.direction = newDirection;
        }
    }

    private boolean isWithinBoundaries(int x, int y, int width, int height) {
        return x >= 0 && y >= 0 && x < width && y < height;
    }

    public boolean checkCollisionWithBoundaries(int width, int height) {
        Point head = body.get(0);
        return !isWithinBoundaries(head.getX(), head.getY(), width, height);
    }

    public void render(GraphicsContext gc, int tileSize) {
        gc.setFill(SNAKE_COLOR);
        for (Point segment : body) {
            gc.fillRect(segment.getX() * tileSize, segment.getY() * tileSize, tileSize, tileSize);
        }
    }


}
