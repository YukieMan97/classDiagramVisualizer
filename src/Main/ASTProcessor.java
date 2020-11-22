import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Optional;

public class ASTProcessor {
    private Hashtable<String, ClassRepresentation> representations;
    private ClassRepresentation curNode;
    private ArrayList<CompilationUnit> classTrees;

    public ASTProcessor() {
        representations = new Hashtable<String, ClassRepresentation>();
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
                 namer.visit(cu, representations);
             }
             for (CompilationUnit cu: cus) {
                 processCompilationUnit(cu);
             }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
         
    }

    private void processCompilationUnit(CompilationUnit cu) {
        MethodProcessor mp = new MethodProcessor();
        mp.visit(cu, null);

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

    private class MethodProcessor extends VoidVisitorAdapter<Void> {
        private String curMethodName;

        @Override
        public void visit(MethodDeclaration md, Void arg) {
            super.visit(md, arg);
            String name = md.getNameAsString();
            curMethodName = name;
            Optional<Node> parentNode = md.getParentNode();
            Node parent = parentNode.get();
            String parentName = ((ClassOrInterfaceDeclaration) parent).getNameAsString();
            String type = md.getType().toString();
            ClassRepresentation parentRep = representations.get(parentName);
            if (representations.containsKey(type)) {
                parentRep.addToClassesReturnedByMethods(type, name);
            }
            NodeList<Parameter> parameters  = md.getParameters();
            for (Parameter p: parameters) {
                String pTypeName = p.getType().toString();
                if (representations.containsKey(pTypeName)) {
                    parentRep.addToClassesUsedAsArguments(pTypeName, name);
                }
                variableDeclarationVisitor vdv = new variableDeclarationVisitor();
                vdv.visit(md, parentRep);
            }
        }
        private class variableDeclarationVisitor extends VoidVisitorAdapter<ClassRepresentation> {
            @Override
            public void visit(VariableDeclarator vd, ClassRepresentation cr) {
                super.visit(vd, cr);
                String typeName = vd.getTypeAsString();
                if (representations.containsKey(typeName)) {
                    cr.addToClassesUsedAsLocalVariables(typeName, curMethodName);
                }
            }
            @Override
            public void visit(VariableDeclarationExpr vde, ClassRepresentation cr) {
                super.visit(vde, cr);
                String vType = vde.getElementType().asString();
                if (representations.containsKey(vType)) {
                    cr.addToClassesUsedAsLocalVariables(vType, curMethodName);
                }
            }
        }
    }


}
