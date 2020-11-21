import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class ASTProcessor {
    private ArrayList<String> nodeNames;
    private ClassNode curNode;

    public ASTProcessor() {
        nodeNames = new ArrayList<String>();
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
        for (Node n : children) {
            if (n instanceof ClassOrInterfaceDeclaration) {
                processClass((ClassOrInterfaceDeclaration) n);
            }
        }


    }

    private void processClass(ClassOrInterfaceDeclaration n) {
        curNode = new ClassNode (n.getNameAsString());
        NodeList<ClassOrInterfaceType> exts = n.getExtendedTypes();
        NodeList<ClassOrInterfaceType> impls = n.getImplementedTypes();
        for (ClassOrInterfaceType e: exts) {
            curNode.addToParentClasses(e.getNameAsString());
        }
        for (ClassOrInterfaceType i: impls) {
            curNode.addToParentInterfaceList(i.getNameAsString());
        }
        List<Node> children = n.getChildNodes();
        for (Node c : children) {
            if (c instanceof FieldDeclaration) {
                processField((FieldDeclaration) c);
            } else if (c instanceof MethodDeclaration) {
                processMethod((MethodDeclaration) c);
            }
        }
    }

    private void processMethod(MethodDeclaration c) {
    }

    private void processField(FieldDeclaration n) {
    }

    private static class methodNameGetter extends VoidVisitorAdapter<Void> {
        @Override public void visit(MethodDeclaration md, Void arg) {
            super.visit(md, arg);
        }
    }
    
}
