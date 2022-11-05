package edu.uncc.hw07.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Forum implements Serializable {
    public String title;
    public String description;
    public String forumCreator;
    public String forumCreatorId;
    public ArrayList<String> likes;
    public String dateTime;
    public String forumId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getForumCreator() {
        return forumCreator;
    }

    public void setForumCreator(String forumCreator) {
        this.forumCreator = forumCreator;
    }

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getForumId() {
        return forumId;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
    }

    public String getForumCreatorId() {
        return forumCreatorId;
    }

    public void setForumCreatorId(String forumCreatorId) {
        this.forumCreatorId = forumCreatorId;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", forumCreator='" + forumCreator + '\'' +
                ", forumCreatorId='" + forumCreatorId + '\'' +
                ", likes=" + likes +
                ", dateTime='" + dateTime + '\'' +
                ", forumId='" + forumId + '\'' +
                '}';
    }
}
