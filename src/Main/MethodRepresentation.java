import java.util.ArrayList;

public class MethodRepresentation {

    private String name;

    private ArrayList<String> usedClasses;
    private ArrayList<String> methodsThatCallThis;
    private boolean isPrivate;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
