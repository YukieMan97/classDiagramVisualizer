import java.util.ArrayList;
import java.util.Hashtable;

public class ClassNode {

    private Hashtable<String,String>  classesUsedAsFields;
    private ArrayList <String> parentClassList;
    private ArrayList <String>  parentInterfaceList;
    //key is class name, value is method name
    private Hashtable <String,String>  classesReturnedByMethods;
    //key is class name, value is name of method that uses it as variable
    private Hashtable <String,String> classesUsedAsLocalVariables;
    //Key is class name, value is name of method that takes it as an argument
    private Hashtable <String,String>classesUsedAsArguments;
    private String name;
    //This stores names of methods, Boolean is true if public, false if private
    private Hashtable<String, Boolean>methods;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private int size;

    public ClassNode(String name) {
        this.name = name;
        this.classesReturnedByMethods = new Hashtable<>();
        this.classesUsedAsArguments = new Hashtable<>();
        this.classesUsedAsFields = new Hashtable<>();
        this.classesUsedAsLocalVariables = new Hashtable<>();
        this.parentClassList = new ArrayList<String>();
        this.parentInterfaceList = new ArrayList<String>();
    }

    public Hashtable<String, String> getClassesUsedAsFields() {
        return this.classesUsedAsFields;
    }

    public ArrayList<String> getParentClassList() {
        return this.parentClassList;
    }

    public ArrayList<String> getParentInterfaceList() {
        return this.parentInterfaceList;
    }

    public Hashtable<String, String>  getClassesReturnedByMethodsList() {
        return this.classesReturnedByMethods;
    }

    public Hashtable<String, String>  getClassesUsedAsLocalVariables() {
        return this.classesUsedAsLocalVariables;
    }

    public Hashtable<String, String>  getClassesUsedAsArguments() {
        return this.classesUsedAsArguments;
    }

    public void addToClassesUsedAsFields(String className, String fieldName) {
        this.classesUsedAsFields.put(className, fieldName);
    }

    public void addToClassesUsedAsArguments(String className, String methodName) {
        this.classesUsedAsArguments.put(className, methodName);
    }

    public void addToClassesUsedAsLocalVariables(String className, String methodName) {
        this.classesUsedAsLocalVariables.put(className, methodName);
    }

    public void addToClassesReturnedByMethods(String className, String methodName) {
        this.classesReturnedByMethods.put(className, methodName);
    }

    public void addToParentClasses(String className) {
        this.parentClassList.add(className);
    }

    public void addToParentInterfaceList(String className) {
        this.parentInterfaceList.add(className);
    }


}
