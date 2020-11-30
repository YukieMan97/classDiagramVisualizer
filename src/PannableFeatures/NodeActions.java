package PannableFeatures;

import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;

/**
 * Listeners that make nodes draggable via left mouse button.
 * Also considers if parent is already zoomed.
 */
public class NodeActions {
    private DragContext nodeDragContext = new DragContext();

    MovableCanvas canvas;

    public NodeActions(MovableCanvas canvas) {
        this.canvas = canvas;
    }

    public EventHandler<MouseEvent> getOnMousePressedEventHandler() {
        return onMousePressedEventHandler;
    }

    public EventHandler<MouseEvent> getOnMouseDraggedEventHandler() {
        return onMouseDraggedEventHandler;
    }

    private EventHandler<MouseEvent> onMousePressedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            // using right mouse button for dragging
            if (!mouseEvent.isSecondaryButtonDown())
                return;

            nodeDragContext.mouseAnchorX = mouseEvent.getSceneX();
            nodeDragContext.mouseAnchorY = mouseEvent.getSceneY();

            Node node = (Node) mouseEvent.getSource();

            nodeDragContext.translateAnchorX = node.getTranslateX();
            nodeDragContext.translateAnchorY = node.getTranslateY();
        }
    };

    private EventHandler<MouseEvent> onMouseDraggedEventHandler = new EventHandler<MouseEvent>() {
        @Override
        public void handle(MouseEvent mouseEvent) {
            // using right mouse button for dragging
            if (!mouseEvent.isSecondaryButtonDown())
                return;

            double scale = canvas.getMovableScale();

            Node node = (Node) mouseEvent.getSource();

            node.setTranslateX(nodeDragContext.translateAnchorX +
                    ((mouseEvent.getSceneX() - nodeDragContext.mouseAnchorX) / scale));
            node.setTranslateY(nodeDragContext.translateAnchorY +
                    ((mouseEvent.getSceneY() - nodeDragContext.mouseAnchorY) / scale));

            mouseEvent.consume();
        }
    };
}
