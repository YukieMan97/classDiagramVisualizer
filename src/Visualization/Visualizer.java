package Visualization;

import Main.*;
import PannableFeatures.MovableCanvas;
import PannableFeatures.SceneActions;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

import static Visualization.VisualizerParams.*;
import static java.lang.StrictMath.abs;
import static java.lang.StrictMath.round;

public class Visualizer {

    private final ASTProcessor astProcessor;

    public static final MovableCanvas canvas = new MovableCanvas();
    public static final ArrayList<ClassObject> parentClasses = new ArrayList<>();

    public Visualizer(Stage mainStage, ASTProcessor astProcessor) {
        // Save references to Canvas and AST Processor
        this.astProcessor = astProcessor;
        // Initialize JavaFX Program
        Group root = new Group();
        // create sample nodes that can be dragged
//        NodeActions nodeActions = new NodeActions(canvas);
//        drawShapes(canvas, nodeActions, root);
        buildVisualization();
        root.getChildren().add(canvas);
        // Set Scene
        Scene scene = new Scene(root, Main.WIDTH, Main.HEIGHT);
        SceneActions sceneActions = new SceneActions(canvas);
        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, sceneActions.getOnMousePressedEventHandler());
        scene.addEventFilter(MouseEvent.MOUSE_DRAGGED, sceneActions.getOnMouseDraggedEventHandler());
        scene.addEventFilter(ScrollEvent.ANY, sceneActions.getOnScrollEventHandler());
        // Set Stage
        mainStage.setTitle("Circles & Squares");
        mainStage.setScene(scene);
        mainStage.show();
//        canvas.createGrid();
    }

    public void buildVisualization() {
        Set<String> classKeys = astProcessor.getClassRepresentations().keySet();
        Hashtable<String, ClassObject> classCircleDictionary = new Hashtable<String, ClassObject>();

        for (String key : classKeys) {

            ClassObject classObj = new ClassObject(key);
            parentClasses.add(classObj);
            classCircleDictionary.put(key, classObj);
        }

        for (String key : classKeys) {

            ClassObject classObj = classCircleDictionary.get(key);
            // step 3: shapes and texts for fields and field names
            ClassRepresentation currentClass = astProcessor.getClassRepresentations().get(key);
            double newRadius = currentClass.getMethods().size()*6 + P_CIRCLE_RADIUS;
            classObj.getClassCircle().setRadius(newRadius);
            for (String field : currentClass.getClassesUsedAsPublicFields().keySet()) {
                String publicFieldName = currentClass.getClassesUsedAsPublicFields().get(field).get(0);
                // create public field node
                Circle publicCircle = createPublicCircle(classObj.xPos, classObj.yPos,
                        C_CIRCLE_RADIUS, publicColor, classObj.getClassCircle(), classObj.getClassName());
                TextObject fieldText = new TextObject(classObj.xPos, classObj.yPos, publicFieldName);
                canvas.getChildren().addAll(publicCircle);

                // handles hovering over children nodes
                registerHandler(canvas, publicCircle, publicColor, circle1HoverColor, fieldText.getText());
            }
            for (String field : currentClass.getClassesUsedAsPrivateFields().keySet()) {
                String privateFieldName = currentClass.getClassesUsedAsPrivateFields().get(field).get(0);
                // create private field node
                Rectangle privateField = createPrivateSquare(classObj.xPos, classObj.yPos,
                        C_SQUARE_SIZE, privateColor, classObj.getClassCircle(), classObj.getClassName());
                TextObject fieldText = new TextObject(classObj.xPos, classObj.yPos, privateFieldName);
                canvas.getChildren().addAll(privateField);

                // handles hovering over children nodes
                registerHandler(canvas, privateField, privateColor, circle1HoverColor, fieldText.getText());
            }
            for (String interfaceName : currentClass.getParentInterfaceList()) {
                connectClasses(canvas, classObj.getClassCircle(), classCircleDictionary.get(interfaceName).getClassCircle(), IMPLEMENTS_COLOR);
            }
            for (String parentClassNAme : currentClass.getParentClassList()) {
                connectClasses(canvas, classObj.getClassCircle(), classCircleDictionary.get(parentClassNAme).getClassCircle(), EXTENDS_COLOR);
            }
            for (String s : currentClass.getClassesReturnedByMethodsList().keySet()) {
                connectClasses(canvas, classObj.getClassCircle(), classCircleDictionary.get(s).getClassCircle(), RETURNED_BY_METHOD_COLOR);
            }
            for (String s : currentClass.getClassesUsedAsArguments().keySet()) {
                connectClasses(canvas, classObj.getClassCircle(), classCircleDictionary.get(s).getClassCircle(), TAKES_AS_ARGUMENT_COLOR);
            }
            for (String s : currentClass.getClassesUsedAsLocalVariables().keySet()) {
                connectClasses(canvas, classObj.getClassCircle(), classCircleDictionary.get(s).getClassCircle(), USED_AS_LOCAL_VARIABLE_COLOR);
            }
            for (String s : currentClass.getClassesUsedAsPublicFields().keySet()) {
                connectClasses(canvas, classObj.getClassCircle(), classCircleDictionary.get(s).getClassCircle(), USED_AS_PUBLIC_FIELD_COLOR);
            }
            for (String s : currentClass.getClassesUsedAsPrivateFields().keySet()) {
                connectClasses(canvas, classObj.getClassCircle(), classCircleDictionary.get(s).getClassCircle(), USED_AS_PRIVATE_FIELD_COLOR);
            }

            // step 4: shapes and texts for methods and method names
            for (MethodRepresentation method : currentClass.getMethods()) {
                addMethod(classObj.getClassCircle(), method.getName(), true, currentClass.getMethods().size());
            }

        }
    }

    private void addMethod(Circle classCircle, String methodName, boolean isPublic, int numMethods) {
        double radius = classCircle.getRadius();
        double randomAngle = ThreadLocalRandom.current().nextDouble(0.0D, 360.0D);
        double classCircleX = classCircle.getCenterX();
        double classCircleY = classCircle.getCenterY();
        double xPos = classCircleX + Math.sin(randomAngle) * radius * 1.5D;
        double yPos = classCircleY + Math.cos(randomAngle) * radius * 1.5D;

        int n = numMethods;
        for(int i = 0; i < n; ++i) {
            Shape methodShape = isPublic ? new Circle() : new Rectangle();
            double methodShapeX = classCircleX  + (radius*1.5) * Math.cos(6.283185307179586D * (double)i / (double)n);
            double methodShapeY = classCircleY  + (radius*1.5) * Math.sin(6.283185307179586D * (double)i / (double)n);
            double methodLineX = classCircleX + radius * Math.cos(6.283185307179586D * (double)i / (double)n);
            double methodLineY = classCircleY + radius * Math.sin(6.283185307179586D * (double)i / (double)n);
            Circle pt = new Circle(methodLineX, methodLineY, 3.0D);
            pt.setFill(Color.BLACK);
            canvas.getChildren().add(pt);

            methodShape.setLayoutX(methodShapeX);
            methodShape.setLayoutY(methodShapeY);
            if (isPublic) {
                ((Circle)methodShape).setRadius(20.0D);
            } else {
                ((Rectangle)methodShape).setHeight(35.0D);
                ((Rectangle)methodShape).setWidth(35.0D);
            }
            Color methodColour = (Color) classCircle.getFill();
            methodShape.setFill(methodColour);
            methodShape.setStrokeWidth(2.5D);
            methodShape.setStroke(STROKE_COLOR);

            Line methodLine = new Line(methodLineX, methodLineY, methodShapeX, methodShapeY);
            if (!isPublic) {
                methodLine.setEndX(methodShapeX + 17.5D);
                methodLine.setEndY(methodShapeY + 17.5D);
            }
            methodLine.setStrokeWidth(2.5D);
            canvas.getChildren().add(methodLine);
            canvas.getChildren().add(methodShape);
            TextObject textObj = new TextObject(methodShapeX, methodShapeY, methodName);
            this.registerHandler(canvas, (Shape)methodShape, methodColour, circle1HoverColor, textObj.getText());
        }

//        TextObject textObj = new TextObject(xPos, yPos, methodName);
//        canvas.getChildren().add(methodShape);

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

    private void connectClasses(AnchorPane canvas, Circle class1, Circle class2, Color colour) {
        Path path = new Path();

        double c1X = class1.getCenterX();
        double c1Y = class1.getCenterY();
        double c1Radius = class1.getRadius();
        double c2X = class2.getCenterX();
        double c2Y = class2.getCenterY();
        double c2Radius = class1.getRadius();
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
        path.setStrokeWidth(10);
        path.setStroke(colour);

        // Create the Hexagon for every  class
        Polygon hexagon = createHexagon(class1);

        registerHandlerExtends(class1, class2, hexagon, path, colour);

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
    private void registerHandlerExtends(Circle circle1, Circle class2, Polygon hexagon, Path path, Color colour) {
        double changeSize = 1;
        Color hoverColor = Color.GOLD;
        Text legendTextObject = new Text(40, 40, "");
        legendTextObject.setFont(new Font("Arial Black", 80));
        legendTextObject.setFill(Color.SALMON);
        legendTextObject.setStroke(Color.BLACK);
        legendTextObject.setStrokeWidth(1);


        hexagon.setOnMouseEntered( mouseEvent -> {
            extendsEnter(mouseEvent, circle1, hexagon, path, changeSize, hoverColor, colour, legendTextObject);

        });

        path.setOnMouseEntered( mouseEvent -> {
            extendsEnter(mouseEvent, circle1, hexagon, path, changeSize, hoverColor, colour, legendTextObject);
        });

        hexagon.setOnMouseExited(mouseEvent -> {
            extendsExit(hexagon, path, changeSize, colour, legendTextObject);
        });

        path.setOnMouseExited( mouseEvent -> {
            extendsExit(hexagon, path, changeSize, colour, legendTextObject);

        });


    }

    // changes path's and hexagon's color and  increases their size
    private void extendsEnter(MouseEvent mouseEvent, Circle superClass, Polygon hexagon, Path path, double changeSize, Color hoverColor, Color colour, Text legendTextObject) {

        path.setStroke(hoverColor);
        path.setStrokeWidth(path.getStrokeWidth() + changeSize);

        hexagon.setScaleX(2);
        hexagon.setScaleY(2);
        hexagon.setStrokeWidth(hexagon.getStrokeWidth() + changeSize);
        hexagon.setFill(hoverColor);

        double mouseX = mouseEvent.getX();
        double mouseY = mouseEvent.getY();
        double added = 1.3;
        double legendX = mouseX + (mouseX * added);
        double legendY = mouseY + (mouseY * added);
        legendTextObject.setX(legendX);
        legendTextObject.setY(legendY);

        if (colour == TAKES_AS_ARGUMENT_COLOR) {
            legendTextObject.setText("takes as argument relationship");
            canvas.getChildren().add(legendTextObject);
        } else if (colour == EXTENDS_COLOR) {
            legendTextObject.setText("Extend Relationship");
            canvas.getChildren().add(legendTextObject);
        } else if (colour == IMPLEMENTS_COLOR) {
            legendTextObject.setText("Implements Relationship");
            canvas.getChildren().add(legendTextObject);
        } else if (colour == USED_AS_LOCAL_VARIABLE_COLOR) {
            legendTextObject.setText("Field is used as Local Variable");
            canvas.getChildren().add(legendTextObject);
        } else if (colour == RETURNED_BY_METHOD_COLOR) {
            legendTextObject.setText("Returned by Method Relationship");
            canvas.getChildren().add(legendTextObject);
        } else if (colour == USED_AS_PUBLIC_FIELD_COLOR) {
            legendTextObject.setText("Used as Public Field Relationship");
            canvas.getChildren().add(legendTextObject);
        } else if (colour == USED_AS_PRIVATE_FIELD_COLOR) {
            legendTextObject.setText("Used as Private Field Relationship");
            canvas.getChildren().add(legendTextObject);
        }
    }

    // changes path's and hexagon's color and  decreases their size
    private void extendsExit(Polygon hexagon, Path path, double changeSize, Color colour, Text legendTextObject) {
        path.setStroke(colour);
        path.setStrokeWidth(path.getStrokeWidth() - changeSize);

        hexagon.setScaleX(1);
        hexagon.setScaleY(1);
        hexagon.setStrokeWidth(hexagon.getStrokeWidth() - changeSize);
        hexagon.setFill(Color.WHITE);

        canvas.getChildren().remove(legendTextObject);
    }
}
