package edu.uncc.hw07.models;

/*
 * Homework 07
 * Comment.java
 * Authors: 1) Sudhanshu Dalvi, 2) Pradip Nemane
 * */

public class Comment {
    public String commentId;
    public String commentCreator;
    public String comment;
    public String creatorId;
    public String dateTime;

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
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

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "commentId='" + commentId + '\'' +
                ", commentCreator='" + commentCreator + '\'' +
                ", comment='" + comment + '\'' +
                ", creatorId='" + creatorId + '\'' +
                ", dateTime='" + dateTime + '\'' +
                '}';
    }
}
