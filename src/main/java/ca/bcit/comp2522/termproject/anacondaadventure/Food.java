package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Food {
    private static final Color COLOR = Color.ROSYBROWN;
    private Point point;

    public Food(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public void render(GraphicsContext gc, int tileSize) {
        gc.setFill(COLOR);
        gc.fillRect(point.getX() * tileSize, point.getY() * tileSize, tileSize, tileSize);
    }
}
