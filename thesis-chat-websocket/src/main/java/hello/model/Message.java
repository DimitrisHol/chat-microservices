package hello.model;

import java.sql.Timestamp;

public class Message {

    private int id;
    private String message;
    private Timestamp timeSent;
    private int authorId;
    private String authorName; // maybe depreciated
    private String type;


    // This is invoked when we get the message from the database.
    public Message(String authorName , String content, Timestamp timestamp) {

        this.message = content;
        this.authorName = authorName;
        this.timeSent = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTimeSent() {
        return timeSent;
    }

    public void setTimeSent(Timestamp timeSent) {
        this.timeSent = timeSent;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", timeSent='" + timeSent + '\'' +
                ", authorName='" + authorName + '\'' +
                '}';
    }
}
