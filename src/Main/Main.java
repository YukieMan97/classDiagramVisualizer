package Main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.stage.Stage;
import pannableFeatures.MovableCanvas;
import pannableFeatures.SceneActions;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Main extends Application {

    public static final double WIDTH = 1024;
    public static final double HEIGHT = 768;
    public static final double P_CIRCLE_RADIUS = 100;                // parent circle
    public static final double C_CIRCLE_RADIUS = 20;                 // child circle
    public static final double C_SQUARE_SIZE = C_CIRCLE_RADIUS + 15; // child square
    private static ASTProcessor astp;

    public static void main(String[] args) {
        FileNavigator FN = new FileNavigator();
        ArrayList<String> paths = new ArrayList<String>();
        FN.getPaths(args[0], paths);
        astp = new ASTProcessor();
        astp.process(paths);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Group root = new Group();

        MovableCanvas canvas = new MovableCanvas();

        // create sample nodes that can be dragged
//        NodeActions nodeActions = new NodeActions(canvas);
//        drawShapes(canvas, nodeActions, root);
        drawShapes(canvas, root);

        root.getChildren().add(canvas);

        Scene scene = new Scene(root, WIDTH, HEIGHT);
        SceneActions sceneActions = new SceneActions(canvas);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneActions.getOnMousePressedEventHandler());
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneActions.getOnMouseDraggedEventHandler());
        scene.addEventFilter(ScrollEvent.ANY, sceneActions.getOnScrollEventHandler());

        primaryStage.setTitle("Circles Circles and Squares");
        primaryStage.setScene(scene);
        primaryStage.show();

//        canvas.createGrid();
    }

    public void drawShapes(AnchorPane canvas, Group root) {
        Color circle1HoverColor = Color.HONEYDEW;
        Color publicColor = Color.SEAGREEN;
        Color privateColor = Color.ROYALBLUE;
        // create parent circle 1, which is basically the first class
        Color circle1ChildrenColor = Color.LIGHTSALMON;
        Circle circle1 = createCircle(100, 100, P_CIRCLE_RADIUS, Color.VIOLET);
        Text text1 = createText(100, 100, "Circle 1");

        // create first child public class circle and text
        Circle publicCircle1a = createPublicCircle(100, 100, C_CIRCLE_RADIUS, publicColor, circle1, text1);
        Text text1a = createText(100, 100, "public method 1");

        // create second child private class square and text
        Rectangle privateSquare1b = createPrivateSquare(100, 100, C_SQUARE_SIZE, privateColor, circle1, text1);
        Text text1b = createText(100, 100, "private method 1");

        // create third child public class circle and text
        Circle publicCircle1c = createPublicCircle(100, 100, C_CIRCLE_RADIUS, publicColor, circle1, text1);
        Text text1c = createText(100, 100, "public method 2");

        // add all nodes related to the first class to the canvas
        canvas.getChildren().addAll(circle1, text1, publicCircle1a, privateSquare1b, publicCircle1c);


        // create parent circle 2, which is basically the second class
        Circle circle2 = createCircle(500, 100, P_CIRCLE_RADIUS, Color.CORNFLOWERBLUE);
        Text text2 = createText(500, 100, "Circle 2");

        // create first child private class square and text
        Rectangle privateSquare2a = createPrivateSquare(500, 100, C_SQUARE_SIZE, privateColor, circle2, text2);
        Text text2a = createText(500, 100, "private method 1");

        // add all nodes related to the second class to the canvas
        canvas.getChildren().addAll(circle2, text2, privateSquare2a);


        // create parent circle 3, which is basically the third class
        Circle circle3 = createCircle(900, 100, P_CIRCLE_RADIUS, Color.CRIMSON);
        Text text3 = createText(900, 100, astp.getRepresentations().get("Program").getName());

        // create first child public class circle and text
        Circle publicCircle3a = createPublicCircle(900, 100, C_CIRCLE_RADIUS, publicColor, circle3, text3);
        Text text3a = createText(900, 100, "public method 1");

        // create second child private class square and text
        Rectangle privateSquare3b = createPrivateSquare(900, 100, C_SQUARE_SIZE, privateColor, circle3, text3);
        Text text3b = createText(900, 100, "private method 1");

        // add all nodes related to the second class to the canvas
        canvas.getChildren().addAll(circle3, text3, publicCircle3a, privateSquare3b);


        // handles hovering over children nodes
        registerHandler(canvas, publicCircle1a, circle1ChildrenColor,circle1HoverColor, text1a);
        registerHandler(canvas, privateSquare1b, circle1ChildrenColor, circle1HoverColor, text1b);
        registerHandler(canvas, publicCircle1c, circle1ChildrenColor, circle1HoverColor, text1c);
        registerHandler(canvas, privateSquare2a, circle1ChildrenColor, circle1HoverColor, text2a);
        registerHandler(canvas, publicCircle3a, circle1ChildrenColor, circle1HoverColor, text3a);
        registerHandler(canvas, privateSquare3b, circle1ChildrenColor, circle1HoverColor, text3b);


        // ignore for now, allows us to move the circle itself
//        circle1.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeActions.getOnMousePressedEventHandler());
//        circle1.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeActions.getOnMouseDraggedEventHandler());
//        circle2.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeActions.getOnMousePressedEventHandler());
//        circle2.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeActions.getOnMouseDraggedEventHandler());
//        circle3.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeActions.getOnMousePressedEventHandler());
//        circle3.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeActions.getOnMouseDraggedEventHandler());
    }

    // create a public circle to represent public methods of the parent class
    private Circle createPublicCircle(double x, double y, double radius, Color c, Circle parentCircle, Text text) {
        // create circle
        final Circle privateCircle = new Circle(x, y, radius, c);
        privateCircle.setStroke(Color.GRAY);
        privateCircle.opacityProperty().set(0.6);
        privateCircle.setStrokeWidth(2.5);
        privateCircle.setStrokeType(StrokeType.INSIDE);

        // set up for random spawning within the parent
        double textW = text.getBoundsInLocal().getWidth();
        double circleRadius = parentCircle.getRadius();
        double min = -1 * (circleRadius - (circleRadius*0.5));
        double max = circleRadius - (circleRadius*0.5);
        double randomX = ThreadLocalRandom.current().nextDouble(min, max);
        double randomY = ThreadLocalRandom.current().nextDouble(min, max);

        // randomX and randomY positions must be within parent circle and not completely overlapping the text
        while (randomX > -1*(textW/2) && randomX < (textW/2)) {
            randomX = ThreadLocalRandom.current().nextDouble(min, max);
        }
        while (randomY < -1*(circleRadius*0.7) && randomY < (circleRadius*0.7)) {
            randomY = ThreadLocalRandom.current().nextDouble(min, max);
        }
        privateCircle.setLayoutX(randomX);
        privateCircle.setLayoutY(randomY);

        return privateCircle;
    }

    // create a private square to represent private methods of parent class
    private Rectangle createPrivateSquare(double x, double y, double radius, Color c, Circle parentCircle, Text text) {
        // create square
        final Rectangle privateSquare = new Rectangle(x, y, radius, radius);
        privateSquare.setFill(c);
        privateSquare.setStroke(Color.GRAY);
        privateSquare.opacityProperty().set(0.6);
        privateSquare.setStrokeWidth(2.5);
        privateSquare.setStrokeType(StrokeType.INSIDE);

        // set up for random spawning within parent circle
        double textW = text.getBoundsInLocal().getWidth();
        double circleRadius = parentCircle.getRadius();
        double min = -1 * (circleRadius - (circleRadius*0.5));
        double max = circleRadius - (circleRadius*0.5);
        double randomX = ThreadLocalRandom.current().nextDouble(min, max);
        double randomY = ThreadLocalRandom.current().nextDouble(min, max);

        // randomX and randomY positions must be within parent circle and not completely overlapping the text
        while (randomX > -1*(textW/2) && randomX < (textW/2)) {
            randomX = ThreadLocalRandom.current().nextDouble(min, max);
        }
        while (randomY < -1*(circleRadius*0.5) && randomY < (circleRadius*0.5)) {
            randomY = ThreadLocalRandom.current().nextDouble(min, max);
        }
        privateSquare.setLayoutX(randomX);
        privateSquare.setLayoutY(randomY);

        return privateSquare;
    }

    private Text createText(double x, double y, String s) {
        final Text text = new Text(s);

        text.setFont(new Font("Tahoma",15));
        text.setBoundsType(TextBoundsType.VISUAL);
        centerText(text, x, y);

        return text;
    }

    private void centerText(Text text, double x, double y) {
        double width = text.getBoundsInLocal().getWidth();
        double height = text.getBoundsInLocal().getHeight();
        text.relocate(x - width / 2, y - height / 2);
    }


    private Circle createCircle(double x, double y, double radius, Color c) {
        final Circle circle = new Circle(x, y, radius, c);
        circle.setStroke(Color.GRAY);
        circle.setStrokeWidth(5);
        circle.setStrokeType(StrokeType.INSIDE);
        return circle;
    }

    // when mouse is hovered over shape, its color and size changes
    // when mouse is not hovered over shape, its original color and size are restored
    private void registerHandler(AnchorPane canvas, Shape s, Color defaultColor, Color hoverColor, Text text) {
        double sizeChange = 5;
        double textX = text.getX();
        double textY = text.getY();
        s.setOnMouseEntered( mouseEvent -> {
            s.setFill(hoverColor);
            if (s instanceof Circle) {
                // increase size of circle
                double radius = ((Circle) s).getRadius();
                ((Circle) s).setRadius(radius + sizeChange);
            } else if (s instanceof  Rectangle) {
                // increase size of square
                double width = ((Rectangle) s).getWidth();
                double height = ((Rectangle) s).getHeight();
                ((Rectangle) s).setWidth(width + sizeChange);
                ((Rectangle) s).setHeight(height + sizeChange);
            }
            // shows name of method
            // problem: keeps updating when mouse is close to text
            text.setX(textX);
            text.setY(textY);
            text.setFont(new Font("Arial Black", 30));
            text.setFill(Color.MISTYROSE);
            text.setStroke(Color.BLACK);
            text.setStrokeWidth(1.5);
            canvas.getChildren().add(text);
        });
        s.setOnMouseExited(mouseEvent -> {
            s.setFill(defaultColor);
            if (s instanceof Circle) {
                // decrease size of circle
                double radius = ((Circle) s).getRadius();
                ((Circle) s).setRadius(radius - sizeChange);
            } else if (s instanceof  Rectangle) {
                // decrease size of square
                double width = ((Rectangle) s).getWidth();
                double height = ((Rectangle) s).getHeight();
                ((Rectangle) s).setWidth(width - sizeChange);
                ((Rectangle) s).setHeight(height - sizeChange);
            }
            // doesn't show name of method anymore
            canvas.getChildren().remove(text);
        });
    }
}
