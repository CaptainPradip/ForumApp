package edu.uncc.hw07.models;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Forum {
    public String  title;
    public  String description;
    public String forumCreator;
    public ArrayList<String> likes;
    public LocalDateTime dateTime;
    public String forumId;

    public String getTitle() {
        return title;
    }

    public String getForumId() {
        return forumId;
    }

    public String getForumCreator() {
        return forumCreator;
    }

    public void setForumCreator(String forumCreator) {
        this.forumCreator = forumCreator;
    }

    public void setForumId(String forumId) {
        this.forumId = forumId;
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

    public ArrayList<String> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<String> likes) {
        this.likes = likes;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    @Override
    public String toString() {
        return "Forum{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", forumCreator='" + forumCreator + '\'' +
                ", likes=" + likes +
                ", dateTime=" + dateTime +
                ", forumId='" + forumId + '\'' +
                '}';
    }
}
