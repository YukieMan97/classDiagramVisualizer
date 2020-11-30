package Main;

import Visualization.Visualizer;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.ArrayList;

public class Main extends Application {

    public static final double WIDTH = 1024;
    public static final double HEIGHT = 768;

    private static ASTProcessor astProcessor;

    public static void main(String[] args) {
        FileNavigator fileNavigator = new FileNavigator();
        ArrayList<String> paths = new ArrayList<String>();
        fileNavigator.getPaths(args[0], paths);
        astProcessor = new ASTProcessor();
        astProcessor.process(paths);
        // Launch Application
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Initialize Visualization w/ AST Processor
        new Visualizer(primaryStage, astProcessor);
    }
}
