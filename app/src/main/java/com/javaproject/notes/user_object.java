package com.javaproject.notes;

public class user_object {
    String id;
    String title;
    String notescontent;
    Boolean likedNote;

    public user_object(String id, String title, String notescontent,Boolean likedNote) {
        this.id = id;
        this.title = title;
        this.notescontent = notescontent;
        this.likedNote = likedNote;
    }

    public user_object() {

    }

    public Boolean getLikedNote() {
        return likedNote;
    }

    public void setLikedNote(Boolean likedNote) {
        this.likedNote = likedNote;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getNotescontent() {
        return notescontent;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setNotescontent(String notescontent) {
        this.notescontent = notescontent;
    }
}
