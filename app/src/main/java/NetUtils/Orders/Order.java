package NetUtils.Orders;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order implements Comparable<Order>, Serializable {
    private Date time;
    private String address;
    private String name;
    private String comment;
    private String link;
    private String color;

    private Boolean isBenefit = false; // !
    private List<Comment> comments;

    public Order(Date time, String address, String name, String comment, String link, String color) {
        this.time = time;
        this.address = address;
        this.name = name;
        this.comment = comment;
        this.link = link;
        this.color = color;
        comments = new ArrayList<Comment>();
    }

    @Override
    public int compareTo(Order o) {
        return (time.after(o.time)) ? 1 : -1;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getIsBenefit() {
        return isBenefit;
    }

    public void setIsBenefit(Boolean isBenefit) {
        this.isBenefit = isBenefit;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

}
