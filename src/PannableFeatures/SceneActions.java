package PannableFeatures;

import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;


/**
 * Listeners that make the canvas of scene draggable and zoomable
 */
public class SceneActions {
    private static final double MAX_SCALE = 10.0d;
    private static final double MIN_SCALE = 0.1d;

    private DragContext sceneDragContext = new DragContext();

    MovableCanvas canvas;

    public SceneActions(MovableCanvas canvas) {
        this.canvas = canvas;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    public EventHandler<ScrollEvent> getOnScrollEventHandler() {
        return onScrollEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            // using left mouse button for panning
            if (!mouseEvent.isPrimaryButtonDown())
                return;

            sceneDragContext.mouseAnchorX = mouseEvent.getSceneX();
            sceneDragContext.mouseAnchorY = mouseEvent.getSceneY();

            sceneDragContext.translateAnchorX = canvas.getTranslateX();
            sceneDragContext.translateAnchorY = canvas.getTranslateY();
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            // use left mouse button for panning
            if (!mouseEvent.isPrimaryButtonDown())
                return;

            canvas.setTranslateX(sceneDragContext.translateAnchorX +
                    mouseEvent.getSceneX() - sceneDragContext.mouseAnchorX);
            canvas.setTranslateY(sceneDragContext.translateAnchorY +
                    mouseEvent.getSceneY() - sceneDragContext.mouseAnchorY);

            mouseEvent.consume();
        }
    };

    // Mouse wheel handler: zoom into pivot point
    private EventHandler<ScrollEvent> onScrollEventHandler = new EventHandler<ScrollEvent>() {
        @Override
        public void handle(ScrollEvent scrollEvent) {
            double delta = 1.05;

            double scale = canvas.getMovableScale();
            double oldScale = scale;

            if (scrollEvent.getDeltaY() < 0) {
                scale /= delta;
            } else {
                scale *= delta;
            }

            scale = clamp(scale, MIN_SCALE, MAX_SCALE);

            double f = (scale/oldScale) - 1;

            double dx = (scrollEvent.getSceneX() - (canvas.getBoundsInParent().getWidth()/2 +
                    canvas.getBoundsInParent().getMinX()));
            double dy = (scrollEvent.getSceneY() - (canvas.getBoundsInParent().getHeight()/2 +
                    canvas.getBoundsInParent().getMinY()));

            canvas.setMovableScale(scale);

            // pivot value must be untransformed (aka without scaling)
            double untransformedX = f*dx;
            double untransformedY = f*dy;
            canvas.setPivot(untransformedX, untransformedY);

            scrollEvent.consume();
        }
    };

    public static double clamp(double value, double min, double max) {
        if (Double.compare(value, min) < 0) {
            return min;
        }

        if (Double.compare(value, max) > 0) {
            return max;
        }

        return value;
    }
}
