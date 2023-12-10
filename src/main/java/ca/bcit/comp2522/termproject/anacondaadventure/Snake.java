package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Represents the snake in the game.
 */
public class Snake {
    private final int WIDTH = AnacondaAdventure.getWIDTH();
    private final int HEIGHT = AnacondaAdventure.getHEIGHT();

    private static List<Point> body;
    private Direction direction;
    private static final Color SNAKE_COLOR = Color.GREEN;

    private int size = 9;

    private Point [] lastPoints = new Point[3];

    /**
     * Constructs a Snake object with default body segments and direction.
     */
    public Snake() {
        body = new ArrayList<>();
        body.add(new Point(5, 5));
        body.add(new Point(5, 6));
        body.add(new Point(5, 7));
        body.add(new Point(5, 8));
        body.add(new Point(5, 9));
        body.add(new Point(5, 10));
        body.add(new Point(5, 11));
        body.add(new Point(5, 12));
        body.add(new Point(5, 13));
        direction = Direction.RIGHT;
    }

    /**
     * Initializes the snake.
     */
    public void init() {

    }


    /**
     * Updates the snake's movement.
     *
     * @param obstacle the obstacle in the game.
     * @return true if the movement is successful, false otherwise.
     */
    public boolean update(Obstacle obstacle) {
        if (!checkCollisionWithBoundaries() && !checkCollisionWithSelf() && !checkCollisionWithObstacle(obstacle)) {
            move();
            return true; // Movement successful
        }
        return false; // Movement failed due to collision
    }

    /**
     * Moves the snake in the current direction.
     * Moves the snake's head in the current direction by one unit, extending the body length.
     * Removes the last segment of the snake's body to simulate movement.
     */
    private void move() {
        Point head = body.get(0);
        Point newHead = new Point(head.getX() + direction.getX(), head.getY() + direction.getY());
        body.add(0, newHead);
        for(int i = 0; i < 3; i++)
            lastPoints[i] = body.get(body.size() - i -1);
        body.remove(body.size() - 1);
    }

    /**
     * Changes the direction of the snake.
     *
     * @param newDirection The new direction to set for the snake.
     *                     If the new direction is not the opposite of the current direction,
     *                     it updates the snake's direction to the new direction.
     */
    public void changeDirection(Direction newDirection) {
        if (!direction.isOpposite(newDirection)) {
            this.direction = newDirection;
        }
    }

    /**
     * Checks if a given point is within the specified boundaries.
     *
     * @param x      The x-coordinate of the point.
     * @param y      The y-coordinate of the point.
     * @param width  The maximum width boundary.
     * @param height The maximum height boundary.
     * @return {@code true} if the point is within the boundaries, {@code false} otherwise.
     */
    private boolean isWithinBoundaries(int x, int y, int width, int height) {
        return x >= 0 && y >= 0 && x <= width && y <= height;
    }

    /**
     * Checks if the snake's head collides with the game boundaries.
     *
     * @return {@code true} if the head collides with the game boundaries, {@code false} otherwise.
     */
    private boolean checkCollisionWithBoundaries() {
        Point head = body.get(0);
        int x = head.getX();
        int y = head.getY();
        return x < 0 || y < 0 || x >= WIDTH || y >= HEIGHT;
    }

    /**
     * Checks if the snake's head collides with its own body segments.
     *
     * @return {@code true} if the head collides with the body segments, {@code false} otherwise.
     */
    private boolean checkCollisionWithSelf() {
        Point head = body.get(0);
        for (int i = 1; i < body.size(); i++) {
            Point segment = body.get(i);
            if (head.getX() == segment.getX() && head.getY() == segment.getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the snake's head collides with any obstacle on the game board.
     *
     * @param obstacle The obstacle object to check collision against.
     * @return {@code true} if the head collides with any obstacle, {@code false} otherwise.
     */
    private boolean checkCollisionWithObstacle(Obstacle obstacle) {
        if (obstacle == null)
            return false;
        for (int i = 0; i < obstacle.getObstacles().size(); i++) {
            Point obs = obstacle.getObstacles().get(i);
            if (getHead().getX() == obs.getX() && getHead().getY() == obs.getY())
                return true;
        }
        return false;
    }

    /**
     * Renders the snake on the game canvas using the provided graphics context and tile size.
     *
     * @param gc       The graphics context used for rendering.
     * @param tileSize The size of each tile on the game canvas.
     */
    public void render(GraphicsContext gc, int tileSize) {
        gc.setFill(SNAKE_COLOR);
        for (Point segment : body) {
            gc.fillRect(segment.getX() * tileSize, segment.getY() * tileSize, tileSize, tileSize);
        }
    }

    /**
     * Increases or decreases the snake size based on the food type eaten and updates game parameters.
     *
     * @param type The type of food eaten.
     * @return {@code true} if the size is successfully updated, {@code false} otherwise.
     */
    public boolean incrementSize(FoodType type){
        if(type == FoodType.NORMAL) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Sound.foodEaten();
                }
            }).start();
            size++;
            AnacondaAdventure.remainingTime += 2;
            if(Objects.equals(AnacondaAdventure.getGameMode(), "Time Attack"))
                AnacondaAdventure.time.setText("" + AnacondaAdventure.remainingTime);
            body.add(lastPoints[2]);
            return true;
        }
        else if(type == FoodType.POWER_UP){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Sound.gameBonus();
                }
            }).start();
            size += 3;
            AnacondaAdventure.remainingTime += 7;
            if(Objects.equals(AnacondaAdventure.getGameMode(), "Time Attack"))
                AnacondaAdventure.time.setText("" + AnacondaAdventure.remainingTime);
            for(int i = 0; i < 3; i++)
                body.add(lastPoints[i]);
            return true;
        }
        else{
            if(size > 3){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Sound.nerf();
                    }
                }).start();
                size -= 2;
                AnacondaAdventure.remainingTime -= 5;
                if(Objects.equals(AnacondaAdventure.getGameMode(), "Time Attack"))
                    AnacondaAdventure.time.setText("" + AnacondaAdventure.remainingTime);
                for (int i = 0; i < 2; i++)
                    body.remove(body.size() - 1- i);
                return true;
            }
            else return false;
        }
    }

    /**
     * Retrieves the head (front) of the snake.
     *
     * @return The head of the snake as a Point object.
     */
    public Point getHead(){
        return body.get(0);
    }

    /**
     * Retrieves the size of the snake.
     *
     * @return The size of the snake.
     */
    public int getSize(){
        return size;
    }

    /**
     * Retrieves the body of the snake.
     *
     * @return The list of points representing the snake's body.
     */
    public List<Point> getBody(){
        return body;
    }

}
