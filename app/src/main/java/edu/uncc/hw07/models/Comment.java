package edu.uncc.hw07.models;

import java.time.LocalDateTime;

public class Comment {
    public  String commentId;
    public String commentCreator;
    public  String comment;
    public LocalDateTime dateTime;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentCreator() {
        return commentCreator;
    }

    public void setCommentCreator(String commentCreator) {
        this.commentCreator = commentCreator;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", commentCreator='" + commentCreator + '\'' +
                ", comment='" + comment + '\'' +
                ", dateTime=" + dateTime +
                '}';
    }
}
