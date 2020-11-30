package Main;

import java.util.ArrayList;
import java.util.Hashtable;

public class MethodRepresentation {

    private String name;

    //Note for below: scope is the name of a method callee, it will be
    //a field name of this methods parent class's field, an argument name
    // or a local variable name, look at output of ASTProcessor's process method for clarity.


    private ArrayList<String> usedClasses;
    //key is method name, value is list of scopes
    private Hashtable<String, ArrayList<String>> methodsThatCallThis;
    //key is scope, value is method name(s)
    private Hashtable<String, ArrayList<String>> methodsThisCalls;
    //key is argument name, value is type
    private Hashtable<String, String> argumentNames;
    //key is localVarName, value is type
    private Hashtable<String, String> localVars;
    //Its own parent class's fields used, not that of any argument or local variable
    private ArrayList<String> usedFields;
    private boolean isPrivate;


    public MethodRepresentation(String name) {
        this.name = name;
        this.usedClasses = new ArrayList<String>();
        this.methodsThatCallThis = new Hashtable<String, ArrayList<String>>();
        this.methodsThisCalls = new Hashtable<String, ArrayList<String>>();
        this.argumentNames = new Hashtable<String, String>();
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

    public Hashtable<String, ArrayList<String>>  getMethodsThatCallThis() {
        return methodsThatCallThis;
    }

    public void addToMethodsThatCallThis(String methodName, String scopeName ) {
        if (!this.methodsThatCallThis.containsKey(methodName)) {
            this.methodsThatCallThis.put(methodName, new ArrayList<String>());
        }
        this.methodsThatCallThis.get(methodName).add(scopeName);
    }

    public Hashtable<String, ArrayList<String>>  getMethodsThisCalls() {
        return methodsThisCalls;
    }

    public void addToMethodsThisCalls(String methodName, String scopeName) {
        if (!this.methodsThisCalls.containsKey(methodName)) {
            this.methodsThisCalls.put(methodName, new ArrayList<String>());
        }
        this.methodsThisCalls.get(methodName).add(scopeName);
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

    public Hashtable<String, String>  getArgumentNames() {
        return this.argumentNames;
    }

    public void addToArgumentNames(String varName, String argType) {
        if (!this.argumentNames.containsKey(varName)) {
            this.argumentNames.put(varName, argType);
        }
    }
}
