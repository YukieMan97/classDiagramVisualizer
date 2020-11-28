import java.util.ArrayList;

public class MethodRepresentation {

    private String name;

    private ArrayList<String> usedClasses;
    private ArrayList<String> methodsThatCallThis;
    private ArrayList<String> methodsThisCalls;
    private boolean isPrivate;

    public MethodRepresentation(String name) {
        this.name = name;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }


    public ArrayList<String> getUsedClasses() {
        return usedClasses;
    }

    public void addToUsedClasses(String usedClass) {
        this.usedClasses.add(usedClass);
    }

    public ArrayList<String> getMethodsThatCallThis() {
        return methodsThatCallThis;
    }

    public void addToMethodsThatCallThis(String methodName) {
        this.methodsThatCallThis.add(methodName);
    }

    public ArrayList<String> getMethodsThisCalls() {
        return methodsThisCalls;
    }

    public void addToMethodsThisCalls(String methodName) {
        this.methodsThisCalls.add(methodName);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
