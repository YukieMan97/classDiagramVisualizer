import java.util.ArrayList;
import java.util.Hashtable;

public class MethodRepresentation {

    private String name;

    private ArrayList<String> usedClasses;
    private ArrayList<String> methodsThatCallThis;
    private ArrayList<String> methodsThisCalls;
    private ArrayList<String> argumentNames;
    //key is localVarName, value is types
    private Hashtable<String, String> localVars;
    //Its own fields used, not that of any argument or local variable
    private ArrayList<String> usedFields;
    private boolean isPrivate;

    public MethodRepresentation(String name) {
        this.name = name;
        this.usedClasses = new ArrayList<String>();
        this.methodsThatCallThis = new ArrayList<String>();
        this.methodsThisCalls = new ArrayList<String>();
        this.argumentNames = new ArrayList<String>();
        this.usedFields = new ArrayList<String>();
        this.localVars = new Hashtable<String, String>();
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

    public void addToUsedClasses(String usedClassName) {
        if (!usedClasses.contains(usedClassName)) {
            this.usedClasses.add(usedClassName);
        }
    }

    public ArrayList<String> getMethodsThatCallThis() {
        return methodsThatCallThis;
    }

    public void addToMethodsThatCallThis(String methodName) {
        if (!this.methodsThatCallThis.contains(methodName)) {
            this.methodsThatCallThis.add(methodName);
        }
    }

    public ArrayList<String> getMethodsThisCalls() {
        return methodsThisCalls;
    }

    public void addToMethodsThisCalls(String methodName) {
        if (!this.methodsThisCalls.contains(methodName)) {
            this.methodsThisCalls.add(methodName);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getUsedFields() {
        return this.getUsedFields();
    }

    public void addToUsedFields(String fieldName) {
        if (!this.usedFields.contains(fieldName)) {
            this.usedFields.add(fieldName);
        }
    }

    public Hashtable<String, String> getLocalVars() {
        return this.localVars;
    }

    public void addToLocalVarNames(String varName, String typeName) {
        if (!this.localVars.containsKey(varName)) {
            localVars.put(varName, typeName);
        }
    }

    public ArrayList<String> getArgumentNames() {
        return this.argumentNames;
    }

    public void addToArgumentNames(String varName) {
        this.argumentNames.add(varName);
    }
}
