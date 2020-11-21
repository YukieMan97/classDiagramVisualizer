import java.util.ArrayList;
import java.util.Hashtable;

public class ClassNode {

    private Hashtable<String,String>  classesUsedAsFields;
    private ArrayList <String> parentClassList;
    private ArrayList <String>  parentInterfaceList;
    private Hashtable <String,String>  classesReturnedByMethods;
    private Hashtable <String,String> classesUsedAsLocalVariables;
    private Hashtable <String,String>classesUsedAsArguments;
    private String name;

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
