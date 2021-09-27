package app.example.studentattendancemanager;

public class ApplicationModel {

    String id;
    String to;
    String from;
    String subject;
    String message;

    public ApplicationModel() {
    }

    public ApplicationModel(String id, String to, String from, String subject, String message) {
        this.id = id;
        this.to = to;
        this.from = from;
        this.subject = subject;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }
}
