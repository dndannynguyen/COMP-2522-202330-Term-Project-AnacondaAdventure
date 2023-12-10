package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.Objects;
import java.util.Random;

/**
 * Represents a food item in the game with various types and rendering functionality.
 */
public class Food {
    private static final Color COLOR_NORMAL = Color.RED;
    private static final Color COLOR_POWER_UP = Color.GOLD;
    private static final Color COLOR_NERF = Color.DARKGRAY;

    private Point point;
    private FoodType type;

    /**
     * Constructs a Food object with a specific point and type.
     *
     * @param point The position of the food
     * @param type  The type of food (NORMAL, POWER_UP, or NERF)
     */
    public Food(Point point, FoodType type) {
        this.point = point;
        this.type = type;
    }

    /**
     * Generates a random food item within specified boundaries.
     *
     * @param minPoint The minimum coordinates where the food can spawn
     * @param maxPoint The maximum coordinates where the food can spawn
     * @param obstacle The obstacle object to avoid for food spawning
     * @param snake    The snake object to avoid for food spawning
     * @param mode     The game mode (Endless or others)
     * @return A Food object with random coordinates and type based on the game mode
     */
    public static Food generateRandomFood(Point minPoint, Point maxPoint, Obstacle obstacle, Snake snake, String mode) {
        Random random = new Random();
        int x, y;
        for (;;){
            x = random.nextInt(maxPoint.getX() - minPoint.getX()) + minPoint.getX();
            y = random.nextInt(maxPoint.getY() - minPoint.getY()) + minPoint.getY();
            if(obstacle == null)
                break;
            if(!checkForObstacles(x, y, obstacle) && !checkForSnake(x, y, snake))
                break;
        }
        if(Objects.equals(mode, "Endless")){
            return new Food(new Point(x, y), FoodType.NORMAL);
        }

        FoodType type = FoodType.NORMAL;
        int foodTypeRandom = random.nextInt(10);
        if (foodTypeRandom == 0) {
            type = FoodType.POWER_UP;
        } else if (foodTypeRandom == 1) {
            type = FoodType.NERF;
        }

        return new Food(new Point(x, y), type);
    }

    /**
     * Checks for obstacles at specified coordinates.
     *
     * @param x        The X-coordinate to check
     * @param y        The Y-coordinate to check
     * @param obstacle The obstacle object to check against
     * @return True if there is an obstacle at the coordinates, False otherwise
     */
    public static boolean checkForObstacles(int x, int y, Obstacle obstacle){
        for(int i = 0; i < obstacle.getObstacles().size(); i++){
            Point obs = obstacle.getObstacles().get(i);
            if(obs.getX() == x && obs.getY() == y)
                return true;
        }
        return false;
    }

    /**
     * Checks for snake presence at specified coordinates.
     *
     * @param x      The X-coordinate to check
     * @param y      The Y-coordinate to check
     * @param snake  The snake object to check against
     * @return True if the snake is at the coordinates, False otherwise
     */
    public static boolean checkForSnake(int x, int y, Snake snake){
        for(int i = 0; i < snake.getSize(); i++){
            Point obs = snake.getBody().get(i);
            if(obs.getX() == x && obs.getY() == y)
                return true;
        }
        return false;
    }

    /**
     * Retrieves the position of the food.
     *
     * @return The point where the food is located
     */
    public Point getPoint() {
        return point;
    }

    /**
     * Sets the position of the food.
     *
     * @param point The new position of the food
     */
    public void setPoint(Point point) {
        this.point = point;
    }

    /**
     * Renders the food on the game canvas.
     *
     * @param gc       The graphics context to render the food
     * @param tileSize The size of each tile on the canvas
     */
    public void render(GraphicsContext gc, int tileSize) {
        switch (type) {
            case NORMAL:
                gc.setFill(COLOR_NORMAL);
                break;
            case POWER_UP:
                gc.setFill(COLOR_POWER_UP);
                break;
            case NERF:
                gc.setFill(COLOR_NERF);
                break;
        }
        gc.fillRect(point.getX() * tileSize, point.getY() * tileSize, tileSize, tileSize);
    }

    /**
     * Retrieves the type of the food.
     *
     * @return The FoodType of this food item
     */
    public FoodType getType(){
        return type;
    }
}
