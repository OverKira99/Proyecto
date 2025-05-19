package com.alejandrobel.proyecto;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import com.alejandrobel.proyecto.utils.FirebaseDataInitializer;


public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initializeFirestoreData();
    }

    private void initializeFirestoreData() {
        FirebaseDataInitializer initializer = new FirebaseDataInitializer(this);

        // Ejecutar en cadena: primero flashcards, luego lecciones
        initializer.initializeFlashcardsIfNeeded()
                .addOnSuccessListener(aVoid -> initializer.initializeLessonsIfNeeded())
                .addOnFailureListener(e -> Log.e(TAG, "Error inicializando datos", e));
    }
}