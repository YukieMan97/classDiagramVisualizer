import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Optional;

public class ASTProcessor {
    private ClassRepresentation curNode;
    private ArrayList<CompilationUnit> classTrees;

    //key  is "<Parent class name> : <method name>" value is method representation, this
    // is so that classes can have methods with the same name;
    private Hashtable<String, MethodRepresentation> methodRepresentations;

    private Hashtable<String, ClassRepresentation> classRepresentations;

    public ASTProcessor() {
        classRepresentations = new Hashtable<String, ClassRepresentation>();
        methodRepresentations = new Hashtable<String, MethodRepresentation>();
        classTrees = new ArrayList<CompilationUnit>();
    }

    private void testMethod() {

    }


    public ArrayList<CompilationUnit> createCompilationUnits(ArrayList<String> paths) throws FileNotFoundException {
        ArrayList<CompilationUnit> results = new ArrayList<CompilationUnit>();
        for (String path : paths) {
            if (path.endsWith(".java")) {
                results.add(StaticJavaParser.parse(new File(path)));
            }
        }
        return results;
    }


    public void process(ArrayList<String> paths) {
        try {
            testMethod();
            ArrayList<CompilationUnit> cus = createCompilationUnits(paths);
            VoidVisitor<Hashtable<String, ClassRepresentation>> namer = new ClassNodeNamer();
            ArrayList<CompilationUnit> classTrees = new ArrayList<CompilationUnit>();
            for (CompilationUnit cu : cus) {
                namer.visit(cu, classRepresentations);
            }
            for (CompilationUnit cu : cus) {
                processCompilationUnit(cu);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void processCompilationUnit(CompilationUnit cu) {
        MethodProcessor mp = new MethodProcessor();
        mp.visit(cu, null);
        FieldProcessor fp = new FieldProcessor();
        fp.visit(cu, null);

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

    private class FieldProcessor extends VoidVisitorAdapter<Void> {

        private VariableDeclarationVisitor vdv = new VariableDeclarationVisitor();
        private String fName;
        private ClassRepresentation currentClassRep;

        @Override
        public void visit(FieldDeclaration fd, Void arg) {
            super.visit(fd, arg);
            Optional<Node> parentNode = fd.getParentNode();
            Node parent = parentNode.get();
            String parentName = ((ClassOrInterfaceDeclaration) parent).getNameAsString();
            currentClassRep = classRepresentations.get(parentName);
            NodeList<Modifier> mods = fd.getModifiers();
            vdv.visit(fd, mods);
        }

        private class TypeVisitor extends VoidVisitorAdapter<NodeList<Modifier>> {
            @Override
            public void visit(ClassOrInterfaceType c, NodeList<Modifier> mods) {
                super.visit(c, mods);
                String name = c.getNameAsString();
                if (classRepresentations.containsKey(name)) {
                    for (Modifier m: mods) {
                        if (m.getKeyword().asString().equalsIgnoreCase("public")) {
                            currentClassRep.addToClassesUsedAsPublicFields(name, fName);
                        } else if (m.getKeyword().asString().equalsIgnoreCase("private")) {
                            currentClassRep.addToClassesUsedAsPrivateFields(name, fName);
                        }
                    }
                }
            }
        }

        private class VariableDeclarationVisitor extends VoidVisitorAdapter<NodeList<Modifier>> {
            private TypeVisitor tv;

            public VariableDeclarationVisitor() {
                super();
                tv = new TypeVisitor();
            }

            @Override
            public void visit(VariableDeclarator vd, NodeList<Modifier> mods) {
                super.visit(vd, mods);
                fName = vd.getNameAsString();
                tv.visit(vd, mods);

            }
        }
    }

        private class MethodProcessor extends VoidVisitorAdapter<Void> {
            private String curMethodName;
            private MethodRepresentation curMethodRep;
            private ClassRepresentation parentClassRep;
            private ArrayList<String> localVars;
           // private ArrayList<>

            @Override
            public void visit(MethodDeclaration md, Void arg) {
                super.visit(md, arg);
              //  MethodRepresentation curMethodRep = new MethodRepresentation();
                String name = md.getNameAsString();
                curMethodName = name;
                curMethodRep = new MethodRepresentation(name);

                Optional<Node> parentNode = md.getParentNode();
                Node parent = parentNode.get();
                String parentName = ((ClassOrInterfaceDeclaration) parent).getNameAsString();
                parentClassRep = classRepresentations.get(parentName);

                methodRepresentations.put(parentClassRep.getName() +": " + curMethodName, curMethodRep);
                String type = md.getType().toString();
                if (classRepresentations.containsKey(type)) {
                    parentClassRep.addToClassesReturnedByMethods(type, name);
                }
                NodeList<Parameter> parameters = md.getParameters();
                for (Parameter p : parameters) {
                    String pTypeName = p.getType().toString();
                    if (classRepresentations.containsKey(pTypeName)) {
                        parentClassRep.addToClassesUsedAsArguments(pTypeName, name);
                        curMethodRep.addToUsedClasses(pTypeName);
                        curMethodRep.addToArgumentNames(name, pTypeName);
                    }
                    variableDeclarationVisitorForLocalVariable vdv = new variableDeclarationVisitorForLocalVariable();
                    vdv.visit(md, parentClassRep);
                }
                UsedFieldsVisitor ufv = new UsedFieldsVisitor();
                ufv.visit(md, curMethodRep);
                MethodCallVisitor mcv = new MethodCallVisitor();
                mcv.visit(md, curMethodRep);
            }

            private class variableDeclarationVisitorForLocalVariable extends VoidVisitorAdapter<ClassRepresentation> {
                private TypeVisitor tv;

                public variableDeclarationVisitorForLocalVariable() {
                    super();
                    tv = new TypeVisitor();
                }

                @Override
                public void visit(VariableDeclarator vd, ClassRepresentation cr) {
                    super.visit(vd, cr);
                    String varName = vd.getNameAsString();

                    tv.visit(vd, varName);
                }

                /*@Override
                public void visit(VariableDeclarationExpr vde, ClassRepresentation cr) {
                    super.visit(vde, cr);
                    tv.visit(vde, cr);
                }*/


                private class TypeVisitor extends VoidVisitorAdapter<String> {

                    @Override
                    public void visit(ClassOrInterfaceType c, String varName ) {
                        super.visit(c, varName);
                        String name = c.getNameAsString();
                        if (classRepresentations.containsKey(name)) {
                            parentClassRep.addToClassesUsedAsLocalVariables(name, curMethodName);
                            curMethodRep.addToLocalVarNames(varName, name);
                        }

                    }
                }

            }

            private class UsedFieldsVisitor extends VoidVisitorAdapter<MethodRepresentation> {
                @Override
                public void visit(FieldAccessExpr fae, MethodRepresentation mr) {
                    super.visit(fae, mr);
                    String calleeName = fae.getScope().toString();
                    if (calleeName.equalsIgnoreCase("this")) {
                        curMethodRep.addToUsedFields(fae.getNameAsString());
                    }
            }

            }

            private class MethodCallVisitor extends VoidVisitorAdapter<MethodRepresentation> {
                @Override
                public void visit(MethodCallExpr call, MethodRepresentation mr) {
                    super.visit(call, mr);
                    String methodName = call.getNameAsString();
                    Optional<Expression> scope = call.getScope();
                    if (scope.equals(null)) {
                        curMethodRep.addToMethodsThisCalls(methodName, curMethodRep.getName());
                    } else {
                        String scopeName = scope.toString();
                        if (curMethodRep.getLocalVars().containsKey(scopeName)) {
                            curMethodRep.addToMethodsThisCalls(methodName, curMethodRep.getLocalVars().get(scopeName));
                            if (methodRepresentations.containsKey(scopeName + ": " + methodName)) {
                                methodRepresentations.get(scopeName + ": " + methodName).addToMethodsThatCallThis(methodName, parentClassRep.getName());
                            }

                        }
                    }
                }

            }

        }


    }
