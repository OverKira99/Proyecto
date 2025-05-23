package com.alejandrobel.proyecto;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.util.Log;

import com.alejandrobel.proyecto.utils.FirebaseDataInitializer;
import com.google.firebase.FirebaseApp;


public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);

    }


}