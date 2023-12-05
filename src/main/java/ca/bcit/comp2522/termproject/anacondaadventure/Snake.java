package ca.bcit.comp2522.termproject.anacondaadventure;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Snake {
    private final int WIDTH = AnacondaAdventure.getWIDTH();
    private final int HEIGHT = AnacondaAdventure.getHEIGHT();

    private static List<Point> body;
    private Direction direction;
    private static final Color SNAKE_COLOR = Color.GREEN;

    private int size = 9;

    private Point [] lastPoints = new Point[3];

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

    public void init() {

    }

    public boolean update(Obstacle obstacle) {
        if (!checkCollisionWithBoundaries(WIDTH, HEIGHT) && !checkCollisionWithSelf() && !checkCollisionWithObstacle(obstacle)) {
            move();
            return true;
        }
        return false;
    }

    private void move() {

        Point head = body.get(0);
        Point newHead = new Point(head.getX() + direction.getX(), head.getY() + direction.getY());
        body.add(0, newHead);
        for(int i = 0; i < 3; i++)
            lastPoints[i] = body.get(body.size() - i -1);
        body.remove(body.size() - 1);
    }

    public void changeDirection(Direction newDirection) {

        if (!direction.isOpposite(newDirection)) {
            this.direction = newDirection;
        }
    }

    private boolean isWithinBoundaries(int x, int y, int width, int height) {
        return x >= 0 && y >= 0 && x <= width && y <= height;
    }

    public boolean checkCollisionWithObstacle(Obstacle obstacle){
        if(obstacle == null)
            return false;
        for(int i = 0; i < obstacle.getObstacles().size(); i++){
            Point obs = obstacle.getObstacles().get(i);
            if(getHead().getX() == obs.getX() && getHead().getY() == obs.getY())
                return true;
        }
        return false;
    }
    public boolean checkCollisionWithBoundaries(int width, int height) {
        Point head = body.get(0);
        int x = head.getX();
        int y = head.getY();
        return !isWithinBoundaries(x, y, width, height);
    }

    public boolean checkCollisionWithSelf() {
        Point head = body.get(0);
        for (int i = 1; i < body.size(); i++) {
            Point segment = body.get(i);
            if (head.getX() == segment.getX() && head.getY() == segment.getY()) {
                return true;
            }
        }
        return false;
    }

    public void render(GraphicsContext gc, int tileSize) {
        gc.setFill(SNAKE_COLOR);
        for (Point segment : body) {
            gc.fillRect(segment.getX() * tileSize, segment.getY() * tileSize, tileSize, tileSize);
        }
    }

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

    public Point getHead(){
        return body.get(0);
    }

    public int getSize(){
        return size;
    }

    public List<Point> getBody(){
        return body;
    }

}
