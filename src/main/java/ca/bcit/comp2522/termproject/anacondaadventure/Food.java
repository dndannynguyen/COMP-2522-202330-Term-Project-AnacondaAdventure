package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Random;

public class Food {
    private static final Color COLOR_NORMAL = Color.ROSYBROWN;
    private static final Color COLOR_POWER_UP = Color.GOLD;
    private static final Color COLOR_NERF = Color.DARKSLATEGRAY;

    private Point point;
    private FoodType type;

    public Food(Point point, FoodType type) {
        this.point = point;
        this.type = type;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

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

    public static Food generateRandomFood(Point minPoint, Point maxPoint) {
        Random random = new Random();
        int x = random.nextInt(maxPoint.getX() - minPoint.getX()) + minPoint.getX();
        int y = random.nextInt(maxPoint.getY() - minPoint.getY()) + minPoint.getY();


        FoodType type = FoodType.NORMAL;
        int foodTypeRandom = random.nextInt(10);
        if (foodTypeRandom == 0) {
            type = FoodType.POWER_UP;
        } else if (foodTypeRandom == 1) {
            type = FoodType.NERF;
        }

        return new Food(new Point(x, y), type);
    }
}
