package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Obstacle {

    private Color OBSTACLE_COLOR = Color.BLUE;
    private static List<Point> obstacles = new ArrayList<>();

    public Obstacle(){
        obstacles = new ArrayList<>();
    }

    public void spawn(Point minPoint, Point maxPoint) {
        Random random = new Random();
        int x, y;
        for (;;){
            x = random.nextInt(maxPoint.getX() - minPoint.getX()) + minPoint.getX();
            y = random.nextInt(maxPoint.getY() - minPoint.getY()) + minPoint.getY();
            if(x != 0 && y!= 2)
                break;
        }
        obstacles.add(new Point(x,y));
    }

    public void render(GraphicsContext gc, int tileSize) {
        gc.setFill(OBSTACLE_COLOR);
        for (Point segment : obstacles) {
            gc.fillRect(segment.getX() * tileSize, segment.getY() * tileSize, tileSize, tileSize);
        }
    }
    public List<Point> getObstacles(){
        return obstacles;
    }
}