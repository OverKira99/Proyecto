package com.alejandrobel.proyecto.lessons.models;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.FieldValue;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Lesson {
    private String id;
    private String title;
    private String description;
    private String videoUrl;
    private int duration; // en segundos
    private Date createdAt;

    public Lesson(String id, String title, String description,
                  String videoUrl, int duration, Date createdAt) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.videoUrl = videoUrl;
        this.duration = duration;
        this.createdAt = createdAt;
    }

    // Getters y setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getVideoUrl() { return videoUrl; }
    public int getDuration() { return duration; }
    public Date getCreatedAt() { return createdAt; }

    public void setId(String id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}