package com.javaproject.notes;

public class user_object {
    String id;
    String title;
    String notescontent;
    Boolean likedNote;
    int color;

    public user_object(String id, String title, String notescontent,Boolean likedNote, int color) {
        this.id = id;
        this.title = title;
        this.notescontent = notescontent;
        this.likedNote = likedNote;
        this.color = color;
    }

    public user_object() {

    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
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
