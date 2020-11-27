package Main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
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

        primaryStage.setTitle("Project 2");
        primaryStage.setScene(scene);
        primaryStage.show();

//        canvas.createGrid();
    }

    public void drawShapes(Pane canvas, Group root) {
        Circle circle1 = createCircle(100, 100, P_CIRCLE_RADIUS, Color.VIOLET);
        Text text1 = createText(100, 100, "Circle 1");
        Circle publicCircle1a = createPublicCircle(100, 100, C_CIRCLE_RADIUS, Color.LIGHTSALMON, circle1, text1);
        Rectangle privateSquare1b = createPrivateSquare(100, 100, C_SQUARE_SIZE, Color.LIGHTSALMON, circle1, text1);
        Circle publicCircle1c = createPublicCircle(100, 100, C_CIRCLE_RADIUS, Color.LIGHTSALMON, circle1, text1);
        canvas.getChildren().addAll(circle1, text1, publicCircle1a, privateSquare1b, publicCircle1c);

        Circle circle2 = createCircle(500, 100, P_CIRCLE_RADIUS, Color.CORNFLOWERBLUE);
        Text text2 = createText(500, 100, "Circle 2");
        Rectangle privateSquare2a = createPrivateSquare(500, 100, C_SQUARE_SIZE, Color.MEDIUMORCHID, circle2, text2);
        canvas.getChildren().addAll(circle2, text2, privateSquare2a);

        Circle circle3 = createCircle(900, 100, P_CIRCLE_RADIUS, Color.CRIMSON);
        Text text3 = createText(900, 100, astp.getRepresentations().get("Program").getName());
        Circle publicCircle3a = createPublicCircle(900, 100, C_CIRCLE_RADIUS, Color.PEACHPUFF, circle3, text3);
        Rectangle privateSquare3b = createPrivateSquare(900, 100, C_SQUARE_SIZE, Color.PEACHPUFF, circle3, text3);
        canvas.getChildren().addAll(circle3, text3, publicCircle3a, privateSquare3b);

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
        final Circle privateCircle = new Circle(x, y, radius, c);
        privateCircle.setStroke(Color.GRAY);
        privateCircle.opacityProperty().set(0.6);
        privateCircle.setStrokeWidth(2.5);
        privateCircle.setStrokeType(StrokeType.INSIDE);

        double textW = text.getBoundsInLocal().getWidth();
        double textH = text.getBoundsInLocal().getHeight();
        double circleRadius = parentCircle.getRadius();
        double min = -1 * (circleRadius - (circleRadius*0.5));
        double max = circleRadius - (circleRadius*0.5);
        double randomX = ThreadLocalRandom.current().nextDouble(min, max);
        double randomY = ThreadLocalRandom.current().nextDouble(min, max);

        // randomX and randomY positions must be within circle and not completely overlapping the text
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
        final Rectangle privateSquare = new Rectangle(x, y, radius, radius);
        privateSquare.setFill(c);
        privateSquare.setStroke(Color.GRAY);
        privateSquare.opacityProperty().set(0.6);
        privateSquare.setStrokeWidth(2.5);
        privateSquare.setStrokeType(StrokeType.INSIDE);

        double textW = text.getBoundsInLocal().getWidth();
        double circleRadius = parentCircle.getRadius();
        double min = -1 * (circleRadius - (circleRadius*0.5));
        double max = circleRadius - (circleRadius*0.5);
        double randomX = ThreadLocalRandom.current().nextDouble(min, max);
        double randomY = ThreadLocalRandom.current().nextDouble(min, max);

        // randomX and randomY positions must be within circle and not completely overlapping the text
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

        text.setFont(new Font(15));
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
}
