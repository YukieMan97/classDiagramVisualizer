package PannableFeatures;

import Main.Main;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;

public class MovableCanvas extends AnchorPane {
    DoubleProperty movableScale = new SimpleDoubleProperty(1.0);

    public MovableCanvas() {
        setPrefSize(Main.WIDTH, Main.HEIGHT);
        setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // apply scale transform
        scaleXProperty().bind(movableScale);
        scaleYProperty().bind(movableScale);
    }

    public void createGrid() {
        double w = getBoundsInLocal().getWidth();
        double h = getBoundsInLocal().getHeight();

        // create grid
        Canvas grid = new Canvas(w, h);

        // don't catch mouse events
        grid.setMouseTransparent(true);

        GraphicsContext gc = grid.getGraphicsContext2D();

        gc.setStroke(Color.TRANSPARENT);
        gc.setLineWidth(1);

        // set up grid lines
        double offset = 50;
        for (double i = offset; i < w; i += offset) {
            gc.strokeLine(i, 0, i, h);
            gc.strokeLine(0, i, w, i);
        }

        getChildren().add(grid);

        grid.toBack();
    }

    public double getMovableScale() {
        return movableScale.get();
    }

    public void setMovableScale(double newScale) {
        movableScale.set(newScale);
    }

    public void setPivot(double x, double y) {
        setTranslateX(getTranslateX()-x);
        setTranslateY(getTranslateY()-y);
    }
}
