import com.github.javaparser.JavaParser;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        FileNavigator FN = new FileNavigator();
        ArrayList<String> paths = new ArrayList<String>();
        FN.getPaths(args[0], paths);
        JavaParser javaParser = new JavaParser();


    }
}
