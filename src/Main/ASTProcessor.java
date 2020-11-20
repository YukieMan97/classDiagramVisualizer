import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ASTProcessor {



    public ArrayList<CompilationUnit> createCompilationUnits(ArrayList<String> paths) throws FileNotFoundException {
        ArrayList<CompilationUnit>results = new ArrayList<CompilationUnit>();
        for (String path : paths) {
            results.add(StaticJavaParser.parse(new File(path)));
        }
        return results;
    }


}
