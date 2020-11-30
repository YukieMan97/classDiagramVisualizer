package Main;

import PannableFeatures.MovableCanvas;
import PannableFeatures.SceneActions;
import javafx.application.Application;
import javafx.geometry.Point2D;
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

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.round;

public class Main extends Application {

    public static final double WIDTH = 1024;
    public static final double HEIGHT = 768;
    public static final double P_CIRCLE_RADIUS = 100;                // parent circle
    public static final double C_CIRCLE_RADIUS = 20;                 // child circle
    public static final double C_SQUARE_SIZE = C_CIRCLE_RADIUS + 15; // child square
    public static final double MIN_RANDOM = -1000;
    public static final double MAX_RANDOM = 1000;
    public static final Color STROKE_COLOR = Color.GRAY;
    public static final Color EXTENDS_COLOR = Color.STEELBLUE;
    private static ASTProcessor astp;
    public static  final Color circle1HoverColor = Color.HONEYDEW;
    public static  final Color publicColor = Color.SEAGREEN;
    public static  final Color privateColor = Color.ROYALBLUE;
    private ArrayList<Circle> parentClasses = new ArrayList<>();

    public static void main(String[] args) {
        FileNavigator FN = new FileNavigator();
        ArrayList<String> paths = new ArrayList<String>();
        FN.getPaths(args[0], paths);
        astp = new ASTProcessor();
        astp.process(paths);
        System.out.println("AST processing complete");
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception{

        Group root = new Group();

        MovableCanvas canvas = new MovableCanvas();

        // create sample nodes that can be dragged
//        NodeActions nodeActions = new NodeActions(canvas);
//        drawShapes(canvas, nodeActions, root);
        drawShapes(canvas);

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

    public void drawShapes(AnchorPane canvas) {
        // randomly generate classes to finish implementation of checkCollision
        // generate random string for texts for now
        Set<String> classKeys = astp.getClassRepresentations().keySet();
        for (String key : classKeys) {
            // step 1: create circle for class
            double randomX = ThreadLocalRandom.current().nextDouble(MIN_RANDOM, MAX_RANDOM);
            double randomY = ThreadLocalRandom.current().nextDouble(MIN_RANDOM, MAX_RANDOM);
            // same radius for all circles
            Circle circle = createClassShape(randomX, randomY);

            double parentX = circle.getCenterX();
            double parentY = circle.getCenterY();

            // step 2: text for class name
            Text className = createText(parentX, parentY, key);
            canvas.getChildren().addAll(circle, className);

            // step 3: shapes and texts for fields and field names
            int numFields = (int) round(ThreadLocalRandom.current().nextDouble(1, 4));
            for (int j = 0; j < numFields; j++) {
                int chooseMethod = (int) round(ThreadLocalRandom.current().nextDouble(0, 1));
                int fieldNum = j + 1;
                if (chooseMethod == 1) {
                    // create public field node
                    Circle publicCircle = createPublicCircle(parentX, parentY, C_CIRCLE_RADIUS, publicColor, circle, className);
                    Text fieldName = createText(parentX, parentY, "field " + fieldNum);
                    canvas.getChildren().addAll(publicCircle);

                    // handles hovering over children nodes
                    registerHandler(canvas, publicCircle, publicColor, circle1HoverColor, fieldName);
                }
                else {
                    // create private field node
                    Rectangle privateField = createPrivateSquare(parentX, parentY, C_SQUARE_SIZE, privateColor, circle, className);
                    Text fieldName = createText(parentX, parentY, "field " + fieldNum);
                    canvas.getChildren().addAll(privateField);

                    // handles hovering over children nodes
                    registerHandler(canvas, privateField, privateColor, circle1HoverColor, fieldName);
                }
            }

            // TODO step 4: shapes and texts for methods and method names (Avi implementing rn)

        }

        // creates connection between superClass (class1) and subClass (class2)
        for (int k = 0; k < 5; k++) {
            int parentSize = parentClasses.size();
            int randomClass1 = (int) round(ThreadLocalRandom.current().nextDouble(0, parentSize-1));
            int randomClass2 = (int) round(ThreadLocalRandom.current().nextDouble(0, parentSize-1));
            if (randomClass1 == (randomClass2)) {
                while (randomClass1 == randomClass2) {
                    randomClass1 = (int) round(ThreadLocalRandom.current().nextDouble(0, parentSize-1));
                    randomClass2 = (int) round(ThreadLocalRandom.current().nextDouble(0, parentSize-1));
                    Circle class1 = parentClasses.get(randomClass1);
                    Circle class2 = parentClasses.get(randomClass2);
                    connectClasses(canvas, class1, class2);
                }
            }
            else {
                Circle class1 = parentClasses.get(randomClass1);
                Circle class2 = parentClasses.get(randomClass2);
                connectClasses(canvas, class1, class2);
            }
        }
    }

    private Circle createClassShape(double x, double y) {
        Circle circle = createCircle(x, y, P_CIRCLE_RADIUS, Color.PEACHPUFF);
        checkCollision(circle);
        parentClasses.add(circle);
        return circle;
    }

    // TODO: once coordinates have changed, need to checkCollision with every parent class again
    private Void checkCollision(Circle circle) {
        if (parentClasses.isEmpty()) {
            return null;
        }
        else {
            boolean keepChecking = true;
//            while (true) {
                for (Circle currCircle : parentClasses) {
                    if (detectCollision(circle, currCircle)) {
                        double randomX = ThreadLocalRandom.current().nextDouble(MIN_RANDOM, MAX_RANDOM);
                        double randomY = ThreadLocalRandom.current().nextDouble(MIN_RANDOM, MAX_RANDOM);
                        circle.setCenterX(randomX);
                        circle.setCenterX(randomY);
                   }
                }
//            }
        }
        return null;
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

        boolean hasCollided = (distance < minDist);
        System.out.println(hasCollided);
        return hasCollided;
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
        path.setStrokeWidth(5);
        path.setStroke(EXTENDS_COLOR);

        // Create the Hexagon for every  class
        Polygon hexagon = createHexagon(superClass);

        registerHandlerExtends(superClass, hexagon, path);

        canvas.getChildren().addAll(hexagon, path);

    }

//     create a public circle to represent public fields of the parent class
    public Circle createPublicCircle(double x, double y, double radius, Color c, Circle parentCircle, Text text) {
        // create circle
        final Circle publicCircle = new Circle(x, y, radius, c);
        publicCircle.setStroke(STROKE_COLOR);
        publicCircle.opacityProperty().set(0.6);
        publicCircle.setStrokeWidth(2.5);
        publicCircle.setStrokeType(StrokeType.INSIDE);

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
        publicCircle.setLayoutX(randomX);
        publicCircle.setLayoutY(randomY);

        return publicCircle;
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

        text.setFont(new Font("Tahoma",12));
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
        text.setX(textX + (textX * 20));
        text.setY(textY + (textY * 20));
        text.setFont(new Font("Arial Black", 30));
        text.setFill(Color.MISTYROSE);
        text.setStroke(Color.BLACK);
        text.setStrokeWidth(1.5);

        s.setOnMouseEntered( mouseEvent -> {
            s.setFill(hoverColor);

            // shows name of field
            // problem: keeps updating when mouse is close to text
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
