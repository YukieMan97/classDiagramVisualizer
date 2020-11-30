package Main;

import javafx.scene.Node;
import javafx.scene.shape.Circle;

public class ClassCircle extends Circle {
    private String className;

    public ClassCircle(String className, double r, double x, double y ) {
        super(r,x,y);
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }


}
