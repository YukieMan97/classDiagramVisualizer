import java.util.ArrayList;
import java.util.Hashtable;

public class ClassRepresentation {

    //key is class name, value is field name
    private Hashtable<String,ArrayList<String>>  classesUsedAsPrivateFields;
    //key is class name, value is field name(s)
    private Hashtable<String,ArrayList<String>>  classesUsedAsPublicFields;

    private ArrayList <String> parentClassList;
    private ArrayList <String>  parentInterfaceList;
    //key is class name, value is methods names
    private Hashtable <String,ArrayList<String>>  classesReturnedByMethods;
    //key is class name, value is names of methods that use it as variable
    private Hashtable <String,ArrayList<String>> classesUsedAsLocalVariables;
    //Key is class name, value is name of method that takes it as an argument
    private Hashtable <String,ArrayList<String>>classesUsedAsArguments;
    private String name;

    private ArrayList<String> methodNames;

    public ArrayList<MethodRepresentation> getMethods() {
        return methods;
    }

    public void addToMethods(MethodRepresentation method) {
        this.methodNames.add(method.getName());
        this.methods.add(method);
    }


    //This stores methodNodes
    private ArrayList<MethodRepresentation> methods;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private int size;

    public ClassRepresentation(String name) {
        this.name = name;
        this.classesReturnedByMethods = new Hashtable<>();
        this.classesUsedAsArguments = new Hashtable<>();
        this.classesUsedAsPublicFields = new Hashtable<>();
        this.classesUsedAsPrivateFields = new Hashtable<>();
        this.classesUsedAsLocalVariables = new Hashtable<>();
        this.parentClassList = new ArrayList<String>();
        this.parentInterfaceList = new ArrayList<String>();
    }

    public Hashtable<String, ArrayList<String>> getClassesUsedAsPublicFields() {
        return this.classesUsedAsPublicFields;
    }

    public Hashtable<String, ArrayList<String>> getClassesUsedAsPrivateFields() {
        return this.classesUsedAsPrivateFields;
    }

    public ArrayList<String> getParentClassList() {
        return this.parentClassList;
    }

    public ArrayList<String> getParentInterfaceList() {
        return this.parentInterfaceList;
    }

    public Hashtable<String, ArrayList<String>>  getClassesReturnedByMethodsList() {
        return this.classesReturnedByMethods;
    }

    public Hashtable<String, ArrayList<String>>  getClassesUsedAsLocalVariables() {
        return this.classesUsedAsLocalVariables;
    }

    public Hashtable<String, ArrayList<String>>  getClassesUsedAsArguments() {
        return this.classesUsedAsArguments;
    }

    public void addToClassesUsedAsPublicFields(String className, String fieldName) {
        if (!classesUsedAsPublicFields.containsKey(className)) {
            this.classesUsedAsPublicFields.put(className, new ArrayList<String>());
        }
        this.classesUsedAsPublicFields.get(className).add(fieldName);
    }

    public void addToClassesUsedAsPrivateFields(String className, String fieldName) {
        if (!classesUsedAsPrivateFields.containsKey(className)) {
            this.classesUsedAsPrivateFields.put(className, new ArrayList<String>());
        }
        this.classesUsedAsPrivateFields.get(className).add(fieldName);
    }

    public void addToClassesUsedAsArguments(String className, String methodName) {
        if (!classesUsedAsArguments.containsKey(className)) {
            this.classesUsedAsArguments.put(className, new ArrayList<String>());
        }
        this.classesUsedAsArguments.get(className).add(methodName);
    }

    public void addToClassesUsedAsLocalVariables(String className, String methodName) {
        if (!this.classesUsedAsLocalVariables.containsKey(className)) {
            this.classesUsedAsLocalVariables.put(className, new ArrayList<String>());
        }
        this.classesUsedAsLocalVariables.get(className).add(methodName);
    }

    public void addToClassesReturnedByMethods(String className, String methodName) {
        if (!this.classesReturnedByMethods.containsKey(className)) {
            this.classesReturnedByMethods.put(className, new ArrayList<String>());
        }
        this.classesReturnedByMethods.get(className).add(methodName);
    }

    public void addToParentClasses(String className) {
        this.parentClassList.add(className);
    }

    public void addToParentInterfaceList(String className) {
        this.parentInterfaceList.add(className);
    }




}
