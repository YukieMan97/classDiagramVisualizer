package Visualization;

import javafx.scene.paint.Color;

public class VisualizerParams {

    public static final double P_CIRCLE_RADIUS = 80;                // parent circle
    public static final double C_CIRCLE_RADIUS = 20;                 // child circle
    public static final double C_SQUARE_SIZE = C_CIRCLE_RADIUS + 15; // child square
    public static final double MIN_RANDOM = -2000;
    public static final double MAX_RANDOM = 2000;
    public static final Color STROKE_COLOR = Color.GRAY;
    public static final Color EXTENDS_COLOR = Color.STEELBLUE;
    public static final Color IMPLEMENTS_COLOR = Color.DIMGRAY;
    public static final Color TAKES_AS_ARGUMENT_COLOR = Color.MEDIUMVIOLETRED;
    public static final Color USED_AS_LOCAL_VARIABLE_COLOR = Color.GOLDENROD;
    public static final Color RETURNED_BY_METHOD_COLOR = Color.SILVER;
    public static final Color USED_AS_PUBLIC_FIELD_COLOR = Color.VIOLET;
    public static final Color USED_AS_PRIVATE_FIELD_COLOR = Color.TURQUOISE;

    public static final Color circle1HoverColor = Color.HONEYDEW;
    public static final Color publicColor = Color.SEAGREEN;
    public static final Color privateColor = Color.ROYALBLUE;
}
