package com.alejandrobel.proyecto.flashcards.models;


import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.io.Serializable;

public class Flashcard implements Serializable {
    // Tus campos existentes...
    // Asegúrate de tener un serialVersionUID para control de versiones
    private static final long serialVersionUID = 1L;
    private String id;
    private String question;
    private String answer;
    private String category;
    private String difficulty;
    private String codeExampleJava;
    private String codeExamplePython;
    private String conclusion;
    private Date createdAt;
    private Date updatedAt;
    private List<String> tags;

    // Constructor vacío requerido por Firestore
    public Flashcard() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getCodeExampleJava() {
        return codeExampleJava;
    }

    public void setCodeExampleJava(String codeExampleJava) {
        this.codeExampleJava = codeExampleJava;
    }

    public String getCodeExamplePython() {
        return codeExamplePython;
    }

    public void setCodeExamplePython(String codeExamplePython) {
        this.codeExamplePython = codeExamplePython;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getTags() { return tags; }
    public void setTags(List<String> tags) { this.tags = tags; }
}