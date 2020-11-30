package Visualization;

import javafx.geometry.Point2D;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import java.util.concurrent.ThreadLocalRandom;

import static Visualization.Visualizer.canvas;
import static Visualization.Visualizer.parentClasses;
import static Visualization.VisualizerParams.*;

public class ClassObject {

    private Circle classCircle;
    private TextObject className;
    public double xPos;
    public double yPos;

    public ClassObject(String name) {
        // step 1: create circle for class
        double randomX = ThreadLocalRandom.current().nextDouble(MIN_RANDOM, MAX_RANDOM);
        double randomY = ThreadLocalRandom.current().nextDouble(MIN_RANDOM, MAX_RANDOM);
        // same radius for all circles
        classCircle = createClassShape(randomX, randomY);

        xPos = classCircle.getCenterX();
        yPos = classCircle.getCenterY();

        // step 2: text for class name
        className = new TextObject(xPos, yPos, name);

        canvas.getChildren().addAll(classCircle, className.getText());
    }

    private Circle createClassShape(double x, double y) {
        Color randomColour = Color.color(Math.random(), Math.random(), Math.random());
        Circle circle = createCircle(x, y, P_CIRCLE_RADIUS, randomColour);
        checkCollision(circle);
        return circle;
    }

    private Circle createCircle(double x, double y, double radius, Color c) {
        final Circle circle = new Circle(x, y, radius, c);
        circle.setStroke(STROKE_COLOR);
        circle.setStrokeWidth(5);
        circle.setStrokeType(StrokeType.INSIDE);

        // add shadow
        DropShadow e = new DropShadow();
        e.setColor(STROKE_COLOR);
        e.setWidth(2);
        e.setHeight(2);
        e.setOffsetX(radius*0.05);
        e.setOffsetY(radius*0.05);
        e.setRadius(15);
        circle.setEffect(e);
        return circle;
    }

    // TODO: once coordinates have changed, need to checkCollision with every parent class again
    private void checkCollision(Circle circle) {
        for (ClassObject currClass : parentClasses) {
            Circle currCircle = currClass.getClassCircle();
            double randomX = ThreadLocalRandom.current().nextDouble(MIN_RANDOM, MAX_RANDOM);
            double randomY = ThreadLocalRandom.current().nextDouble(MIN_RANDOM, MAX_RANDOM);
            circle.setCenterX(randomX);
            circle.setCenterX(randomY);
            boolean hit = detectCollision(circle, currCircle);
            while (hit) {
                randomX = ThreadLocalRandom.current().nextDouble(MIN_RANDOM, MAX_RANDOM);
                randomY = ThreadLocalRandom.current().nextDouble(MIN_RANDOM, MAX_RANDOM);
                circle.setCenterX(randomX);
                circle.setCenterX(randomY);
                hit = detectCollision(circle, currCircle);
            }
        }
    }

    public boolean detectCollision(Circle circle, Circle currCircle) {

        // determine it's size
        Point2D otherCenter = circle.localToScene(circle.getCenterX(), circle.getCenterY());
        Point2D thisCenter = currCircle.localToScene(currCircle.getCenterX(), currCircle.getCenterY());
        double dx = otherCenter.getX() - thisCenter.getX();
        double dy = otherCenter.getY() - thisCenter.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        double minDist = circle.getRadius() + currCircle.getRadius();

//        System.out.println("-------------------");
//        System.out.println("circle.localToScene(...).getX() & .getY(): [" + otherCenter.getX() + ", " + otherCenter.getY() + "]" );
//        System.out.println("circle.getLayoutX() & .getLayoutY(): [" + circle.getLayoutX() + ", " + circle.getLayoutY() + "]" );
//        System.out.println("circle.getCenterX() & .getCenterY(): [" + circle.getCenterX() + ", " + circle.getCenterY() + "]" );
//        System.out.println("currCircle.localToScene(...).getX() & .getY(): [" + thisCenter.getX() + ", " + thisCenter.getY() + "]" );
//        System.out.println("currCircle.getLayoutX() & .getLayoutY(): [" + currCircle.getLayoutX() + ", " + currCircle.getLayoutY() + "]" );
//        System.out.println("currCircle.getCenterX() & .getCenterY(): [" + currCircle.getCenterX() + ", " + currCircle.getCenterY() + "]" );
//        System.out.println("-------------------");

        return (distance < minDist);
    }

    public Circle getClassCircle() {
        return classCircle;
    }

    public Text getClassName() {
        return className.getText();
    }
}
