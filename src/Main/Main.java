package Main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;
import pannableFeatures.MovableCanvas;
import pannableFeatures.SceneActions;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.StrictMath.abs;

public class Main extends Application {

    public static final double WIDTH = 1024;
    public static final double HEIGHT = 768;
    public static final double P_CIRCLE_RADIUS = 100;                // parent circle
    public static final double C_CIRCLE_RADIUS = 20;                 // child circle
    public static final double C_SQUARE_SIZE = C_CIRCLE_RADIUS + 15; // child square
    public static final Color STROKE_COLOR = Color.GRAY;
    public static final Color EXTENDS_COLOR = Color.STEELBLUE;
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
        Circle circle1 = createCircle(100, 300, P_CIRCLE_RADIUS, Color.PALEGREEN);
        Text text1 = createText(100, 300, "Circle 1");


        // create first child public class circle and text
        Circle publicCircle1a = createPublicCircle(100, 300, C_CIRCLE_RADIUS, publicColor, circle1, text1);
        Text text1a = createText(100, 300, "public field 1");


        // create second child private class square and text
        Rectangle privateSquare1b = createPrivateSquare(100, 300, C_SQUARE_SIZE, privateColor, circle1, text1);
        Text text1b = createText(100, 300, "private field 1");

        // create third child public class circle and text
        Circle publicCircle1c = createPublicCircle(100, 300, C_CIRCLE_RADIUS, publicColor, circle1, text1);
        Text text1c = createText(100, 300, "public field 2");

        // add all nodes related to the first class to the canvas
        canvas.getChildren().addAll(circle1, publicCircle1a, privateSquare1b, publicCircle1c, text1);

        // create parent circle 2, which is basically the second class
        Circle circle2 = createCircle(500, 300, P_CIRCLE_RADIUS, Color.LAVENDER);
        Text text2 = createText(500, 300, "Circle 2");

        // create first child private class square and text
        Rectangle privateSquare2a = createPrivateSquare(500, 300, C_SQUARE_SIZE, privateColor, circle2, text2);
        Text text2a = createText(500, 300, "private field 1");

        // add all nodes related to the second class to the canvas
        canvas.getChildren().addAll(circle2, privateSquare2a, text2);


        // create parent circle 3, which is basically the third class
        Circle circle3 = createCircle(900, 300, P_CIRCLE_RADIUS, Color.PEACHPUFF);
        Text text3 = createText(900, 300, astp.getRepresentations().get("Program").getName());

        // create first child public class circle and text
        Circle publicCircle3a = createPublicCircle(900, 300, C_CIRCLE_RADIUS, publicColor, circle3, text3);
        Text text3a = createText(900, 300, "public field 1");

        // create second child private class square and text
        Rectangle privateSquare3b = createPrivateSquare(900, 300, C_SQUARE_SIZE, privateColor, circle3, text3);
        Text text3b = createText(900, 300, "private field 1");

        // add all nodes related to the second class to the canvas
        canvas.getChildren().addAll(circle3, publicCircle3a, privateSquare3b, text3);


        // takes in a canvas, superClass, subClass to connect superClass to subClass
        // uses EXTENDS_COLOR
        connectClasses(canvas, circle1, circle3);
        connectClasses(canvas, circle2, circle3);


        // handles hovering over children nodes
        registerHandler(canvas, publicCircle1a, publicColor,circle1HoverColor, text1a);
        registerHandler(canvas, privateSquare1b, privateColor, circle1HoverColor, text1b);
        registerHandler(canvas, publicCircle1c, publicColor, circle1HoverColor, text1c);
        registerHandler(canvas, privateSquare2a, privateColor, circle1HoverColor, text2a);
        registerHandler(canvas, publicCircle3a, publicColor, circle1HoverColor, text3a);
        registerHandler(canvas, privateSquare3b, privateColor, circle1HoverColor, text3b);


        // ignore for now, allows us to move the circle itself
//        circle1.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeActions.getOnMousePressedEventHandler());
//        circle1.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeActions.getOnMouseDraggedEventHandler());
//        circle2.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeActions.getOnMousePressedEventHandler());
//        circle2.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeActions.getOnMouseDraggedEventHandler());
//        circle3.addEventFilter(MouseEvent.MOUSE_PRESSED, nodeActions.getOnMousePressedEventHandler());
//        circle3.addEventFilter(MouseEvent.MOUSE_DRAGGED, nodeActions.getOnMouseDraggedEventHandler());
    }


    // create hexagon symbol as a representation of a superClass
    private Polygon createHexagon(Circle superClass) {
        double radius = superClass.getRadius();
        Polygon hexagon = new Polygon(
                0, -30,
                20, -10,
                20, 10,
                0, 30,
                -20, 10,
                -20, -10);
        hexagon.setLayoutX(superClass.getCenterX());
        hexagon.setLayoutY(superClass.getCenterY() - radius);

        double desiredScale = 0.9;
        Scale scale = setHexagonScale(superClass, hexagon, desiredScale);

        //Adding the scale transformation to circle1
        hexagon.getTransforms().add(scale);
        hexagon.setFill(Color.WHITE);
        hexagon.setStroke(Color.BLACK);

        return hexagon;
    }

    // returns desired scale for a hexagon
    private Scale setHexagonScale(Circle superClass, Polygon hexagon, double desiredScale) {
        double radius = superClass.getRadius();
        Scale scale = new Scale();

        // set up scale ratio
        double hexagonLength = abs(hexagon.getPoints().get(1)) +  hexagon.getPoints().get(7);
        double factor = (desiredScale*(radius*2))/hexagonLength;
        double scaleRatio = (hexagonLength*factor)/(radius*2);

        //Setting the dimensions for the transformation
        scale.setX(scaleRatio);
        scale.setY(scaleRatio);
        return scale;
    }

    private void connectClasses(AnchorPane canvas, Circle superClass, Circle subClass) {
        Path path = new Path();

        double c1X = superClass.getCenterX();
        double c1Y = superClass.getCenterY();
        double c1Radius = superClass.getRadius();
        double c2X = subClass.getCenterX();
        double c2Y = subClass.getCenterY();
        double c2Radius = superClass.getRadius();
        double midPointX = c1X+c2X/2;
        double midPointY = midPointX * - 0.4;

        MoveTo moveTo = new MoveTo();
        moveTo.setX(c1X);
        moveTo.setY(c1Y-c1Radius);

        QuadCurveTo quadTo = new QuadCurveTo();
        quadTo.setControlX(midPointX);
        quadTo.setControlY(midPointY);
        quadTo.setX(c2X);
        quadTo.setY(c2Y-c2Radius);

        path.getElements().add(moveTo);
        path.getElements().add(quadTo);
        path.setStrokeWidth(2.5);
        path.setStroke(EXTENDS_COLOR);

        // Create the Hexagon for every  class
        Polygon hexagon = createHexagon(superClass);

        registerHandlerExtends(superClass, hexagon, path);

        canvas.getChildren().addAll(hexagon, path);

    }

    // create a public circle to represent public fields of the parent class
    private Circle createPublicCircle(double x, double y, double radius, Color c, Circle parentCircle, Text text) {
        // create circle
        final Circle privateCircle = new Circle(x, y, radius, c);
        privateCircle.setStroke(STROKE_COLOR);
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

    // create a private square to represent private fields of parent class
    private Rectangle createPrivateSquare(double x, double y, double radius, Color c, Circle parentCircle, Text text) {
        // create square
        final Rectangle privateSquare = new Rectangle(x, y, radius, radius);
        privateSquare.setFill(c);
        privateSquare.setStroke(STROKE_COLOR);
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

    // When mouse is hovered over shape, changes size, color, and shows text
    // Shapes return to default properties when mouse is not hovered anymore
    private void registerHandler(AnchorPane canvas, Shape s, Color defaultColor, Color hoverColor, Text text) {
        double sizeChange = 5;
        double textX = text.getX();
        double textY = text.getY();

        s.setOnMouseEntered( mouseEvent -> {
            s.setFill(hoverColor);

            // shows name of field
            // problem: keeps updating when mouse is close to text
            text.setX(textX + (textX * 20));
            text.setY(textY - (textX * 20));
            text.setFont(new Font("Arial Black", 30));
            text.setFill(Color.MISTYROSE);
            text.setStroke(Color.BLACK);
            text.setStrokeWidth(1.5);
            canvas.getChildren().add(text);

            // increases size of shape when hovered
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
        });

        s.setOnMouseExited(mouseEvent -> {
            s.setFill(defaultColor);

            // doesn't show name of field anymore
            canvas.getChildren().remove(text);

            // decreases size of shape when hovered
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
        });
    }

    // When mouse is hovered over path, hexagon expands to show the path from superClass to subClass
    private void registerHandlerExtends(Circle superClass, Polygon hexagon, Path path) {
        double changeSize = 1;
        Color hoverColor = Color.GOLD;

        hexagon.setOnMouseEntered( mouseEvent -> {
            extendsEnter(superClass, hexagon, path, changeSize, hoverColor);

        });

        path.setOnMouseEntered( mouseEvent -> {
            extendsEnter(superClass, hexagon, path, changeSize, hoverColor);

        });

        hexagon.setOnMouseExited(mouseEvent -> {
            extendsExit(hexagon, path, changeSize);
        });

        path.setOnMouseExited( mouseEvent -> {
            extendsExit(hexagon, path, changeSize);

        });


    }

    // changes path's and hexagon's color and  increases their size
    private void extendsEnter(Circle superClass, Polygon hexagon, Path path, double changeSize, Color hoverColor) {
        path.setStroke(hoverColor);
        path.setStrokeWidth(path.getStrokeWidth() + changeSize);

        hexagon.setScaleX(2);
        hexagon.setScaleY(2);
        hexagon.setStrokeWidth(hexagon.getStrokeWidth() + changeSize);
        hexagon.setFill(hoverColor);
    }

    // changes path's and hexagon's color and  decreases their size
    private void extendsExit(Polygon hexagon, Path path, double changeSize) {
        path.setStroke(EXTENDS_COLOR);
        path.setStrokeWidth(path.getStrokeWidth() - changeSize);

        hexagon.setScaleX(1);
        hexagon.setScaleY(1);
        hexagon.setStrokeWidth(hexagon.getStrokeWidth() - changeSize);
        hexagon.setFill(Color.WHITE);
    }

}
