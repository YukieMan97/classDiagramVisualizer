import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.type.Type;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;

public class ASTProcessor {
    private Hashtable<String, ClassRepresentation> nodes;
    private ClassRepresentation curNode;
    private ArrayList<CompilationUnit> classTrees;

    public ASTProcessor() {
        nodes = new Hashtable<String, ClassRepresentation>();
        classTrees = new ArrayList<CompilationUnit>();
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
             VoidVisitor<Hashtable<String, ClassRepresentation>> namer = new ClassNodeNamer();
             ArrayList<CompilationUnit> classTrees = new ArrayList<CompilationUnit>();
             for (CompilationUnit cu: cus) {
                 namer.visit(cu, nodes);
             }
             for (CompilationUnit cu: cus) {
                 processCompilationUnit(cu);
             }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
         
    }

    private void processCompilationUnit(CompilationUnit cu) {

    }

    private static class ClassNodeNamer extends VoidVisitorAdapter<Hashtable<String, ClassRepresentation>> {

        @Override
        public void visit(ClassOrInterfaceDeclaration cd, Hashtable<String, ClassRepresentation> nodes) {
            super.visit(cd, nodes);
            String name = cd.getNameAsString();
            ClassRepresentation cn = new ClassRepresentation(name);
            NodeList<ClassOrInterfaceType> exts = cd.getExtendedTypes();
            for (ClassOrInterfaceType t : exts) {
                cn.addToParentClasses(t.getNameAsString());
            }
            NodeList<ClassOrInterfaceType> impls = cd.getImplementedTypes();
            for (ClassOrInterfaceType t : impls) {
                cn.addToParentInterfaceList(t.getNameAsString());
            }
            nodes.put(name, cn);
        }
    }

    private class MethodProcessor extends VoidVisitorAdapter<ClassRepresentation> {
        @Override
        public void visit(MethodDeclaration md, ClassRepresentation cn) {
            super.visit(md, cn);
            String name = md.getNameAsString();
            //ClassOrInterfaceDeclaration parentNode = md.getParentNode();
            Type type = md.getType();
            NodeList<Parameter> parameters  = md.getParameters();
            if (nodes.containsKey(type)) {

            }

        }
    }

   /* public ArrayList<ClassNode> makeNodesFromCompilationUnits(ArrayList<CompilationUnit> cus) {
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
*/

   // }

    /*private void processClass(ClassOrInterfaceDeclaration n) {
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
        nodes.put(n.getNameAsString(), curNode);
    }

    private void processMethod(MethodDeclaration c) {
    }

    private void processField(FieldDeclaration n) {
    }

    private class methodDeclarationAdaptor extends VoidVisitorAdapter<Void> {
        @Override public void visit(MethodDeclaration md, Void arg) {
            super.visit(md, arg);
            NodeList<Parameter> parameters =  md.getParameters();
            if (nodes.contains md.getTypeAsString())
            for (Parameter p: parameters) {
                Type type = p.getType();
                if (nodes.containsKey(type.toString()) {
                    curNode.addToClassesUsedAsArguments(type.toString(), md.getNameAsString());
                }
            }
        }
    }
    */
}
