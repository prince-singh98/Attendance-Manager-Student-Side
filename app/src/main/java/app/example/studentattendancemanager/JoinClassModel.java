package app.example.studentattendancemanager;

public class JoinClassModel {

    String classCode;
    String id;
    int roll;
    String name;
    String className;


    public JoinClassModel() {
    }

    public JoinClassModel(String classCode, String id, int roll, String name, String className) {
        this.classCode = classCode;
        this.id = id;
        this.roll = roll;
        this.name = name;
        this.className = className;
    }

    public String getClassCode() {
        return classCode;
    }

    public String getId() {
        return id;
    }

    public int getRoll() {
        return roll;
    }

    public String getName() {
        return name;
    }

    public String getClassName() {
        return className;
    }
}
