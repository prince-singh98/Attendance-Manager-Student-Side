package app.example.studentattendancemanager;

public class AttendanceModel {

    String currentDate;
    int roll;
    int value;

    public AttendanceModel() {
    }

    public AttendanceModel(String currentDate, int roll, int value) {
        this.currentDate = currentDate;
        this.roll = roll;
        this.value = value;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public int getRoll() {
        return roll;
    }

    public int getValue() {
        return value;
    }

}
