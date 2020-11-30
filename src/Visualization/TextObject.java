package Visualization;

import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import static Visualization.Visualizer.canvas;

public class TextObject {

    private Text text;

    public TextObject(double parentX, double parentY, String key) {
        text = createText(parentX, parentY, key);
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

    public Text getText() {
        return text;
    }
}
