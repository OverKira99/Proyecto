package com.alejandrobel.proyecto.flashcards.models;


import android.widget.RadioGroup;

import java.io.Serializable;
import java.security.Timestamp;
import java.util.Date;
import java.util.List;
public class Flashcard {
    private String id;
    private String pregunta;
    private String descripcion;
    private String titulo;
    private boolean isTrue;
    private String feedback;
    private String dificultad;
    private boolean isMastered;

    public Flashcard() {}

    public Flashcard(String id, String pregunta, String descripcion, String titulo,
                     boolean isTrue, String feedback, String dificultad, boolean isMastered) {
        this.id = id;
        this.pregunta = pregunta;
        this.descripcion = descripcion;
        this.titulo = titulo;
        this.isTrue = isTrue;
        this.feedback = feedback;
        this.dificultad = dificultad;
        this.isMastered = isMastered;
    }

    public String getId() { return id; }
    public String getPregunta() { return pregunta; }
    public String getDescripcion() { return descripcion; }
    public String getTitulo() { return titulo; }
    public boolean getIsTrue() { return isTrue; }
    public String getFeedback() { return feedback; }
    public String getDificultad() { return dificultad; }
    public boolean isMastered() { return isMastered; }

    public void setId(String id) { this.id = id; }
    public void setPregunta(String pregunta) { this.pregunta = pregunta; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setIsTrue(boolean isTrue) { this.isTrue = isTrue; }
    public void setFeedback(String feedback) { this.feedback = feedback; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }
    public void setMastered(boolean mastered) { this.isMastered = mastered; }
}