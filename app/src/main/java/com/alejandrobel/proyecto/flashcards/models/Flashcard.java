package com.alejandrobel.proyecto.flashcards.models;


import android.widget.RadioGroup;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.Date;
import java.util.List;

public class Flashcard {
    private String title;
    private String description;
    private String question;
    private boolean correctAnswer;
    private Boolean userAnswer;
    private String difficulty;


    public Flashcard(String title, String description, String question,
                     boolean correctAnswer, String difficulty) {
        this(title, description, question, correctAnswer, null, difficulty);
    }

    public Flashcard(String title, String description, String question,
                     boolean correctAnswer, Boolean userAnswer, String difficulty) {
        this.title = title;
        this.description = description;
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.userAnswer = userAnswer;
        this.difficulty = difficulty;
    }


    // Getters y setters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getQuestion() { return question; }
    public boolean isCorrectAnswer() { return correctAnswer; }
    public Boolean getUserAnswer() { return userAnswer; }
    public void setUserAnswer(Boolean userAnswer) { this.userAnswer = userAnswer; }
    public String getDifficulty() { return difficulty; }
}