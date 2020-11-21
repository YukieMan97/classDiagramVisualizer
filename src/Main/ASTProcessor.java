import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ASTProcessor {

    public ASTProcessor() {

    }



    public ArrayList<CompilationUnit> createCompilationUnits(ArrayList<String> paths) throws FileNotFoundException {
        ArrayList<CompilationUnit>results = new ArrayList<CompilationUnit>();
        for (String path : paths) {
            if (path.endsWith(".java")) {
                results.add(StaticJavaParser.parse(new File(path)));
            }
        }
        return results;
    }


    public void process(ArrayList<String> paths) {
         try {
             ArrayList<CompilationUnit> cus = createCompilationUnits(paths);
             ArrayList<ClassNode> nodes = makeNodesFromCompilationUnits(cus);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
         
    }

    public ArrayList<ClassNode> makeNodesFromCompilationUnits(ArrayList<CompilationUnit> cus) {
        ArrayList<ClassNode> result = new ArrayList<ClassNode>();
        for (CompilationUnit cu : cus) {
            makeClassNode(cu);
        }
        return new ArrayList<ClassNode>();
    }

    public void makeClassNode(CompilationUnit cu) {
        List<Node> children = cu.getChildNodes();


    }
}
