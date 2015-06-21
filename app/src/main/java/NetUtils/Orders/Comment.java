package NetUtils.Orders;

import java.util.Date;

public class Comment {
    Date date;
    String user;
    String event;
    String message;
    String color;

    public Comment(Date date, String user, String event, String message, String color) {
        this.date = date;
        this.user = user;
        this.event = event;
        this.message = message;
        this.color = color;
    }

    public String getMessage() {
        return message;
    }

    public String getEvent() {
        return event;
    }

    public Date getDate() {
        return date;
    }

    public String getUser() {
        return user;
    }

    public String getColor() {
        return color;
    }
}
