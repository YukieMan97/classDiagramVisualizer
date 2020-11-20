import java.util.ArrayList;

public class ClassNode {

    private ArrayList<String> classesUsedAsFields;
    private ArrayList<String> parentClassList;
    private ArrayList<String> parentInterfaceList;
    private ArrayList<String> classesReturnedByMethods;
    private ArrayList<String> classesUsedAsLocalVariables;
    private ArrayList<String> classesUsedAsArguments;
    private String name;

    public ClassNode(String name) {
        this.name = name;
        this.classesReturnedByMethods = new ArrayList<String>();
        this.classesUsedAsArguments = new ArrayList<String>();
        this.classesUsedAsFields = new ArrayList<String>();
        this.classesUsedAsLocalVariables = new ArrayList<String>();
        this.parentClassList = new ArrayList<String>();
        this.parentInterfaceList = new ArrayList<String>();
    }

    public ArrayList<String> getClassesUsedAsFields() {
        return this.classesUsedAsFields;
    }

    public ArrayList<String> getParentClassList() {
        return this.parentClassList;
    }

    public ArrayList<String> getParentInterfaceList() {
        return this.parentInterfaceList;
    }

    public ArrayList<String> getClassesReturnedByMethodsList() {
        return this.classesReturnedByMethods;
    }

    public ArrayList<String> getClassesUsedAsLocalVariables() {
        return this.classesUsedAsLocalVariables;
    }

    public ArrayList<String> getClassesUsedAsArguments() {
        return this.classesUsedAsArguments;
    }

    public void addToClassesUsedAsFields(String className) {
        this.classesUsedAsFields.add(className);
    }

    public void addToClassesUsedAsArguments(String className) {
        this.classesUsedAsArguments.add(className);
    }

    public void addToClassesUsedAsLocalVariables(String className) {
        this.classesUsedAsLocalVariables.add(className);
    }

    public void addToClassesReturnedByMethods(String className) {
        this.classesReturnedByMethods.add(className);
    }

    public void addToParentClasses(String className) {
        this.parentClassList.add(className);
    }

    public void addToParentInterfaceList(String className) {
        this.parentInterfaceList.add(className);
    }


}
