package com.alejandrobel.proyecto.auth.repositories;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AuthRepository {
    private final FirebaseAuth mAuth;
    private static final String TAG = "AuthRepository";

    public AuthRepository() {
        mAuth = FirebaseAuth.getInstance();
    }
    // Login con Email/Contraseña
    public void loginWithEmail(String email, String password, AuthCallback callback) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "loginWithEmail:success");
                        callback.onSuccess();
                    } else {
                        Log.w(TAG, "loginWithEmail:failure", task.getException());
                        callback.onError(task.getException());
                    }
                });
    }
    // Login con Google
    public void loginWithGoogle(String idToken, AuthCallback callback) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "loginWithGoogle:success");
                        callback.onSuccess();
                    } else {
                        Log.w(TAG, "loginWithGoogle:failure", task.getException());
                        callback.onError(task.getException());
                    }
                });
    }
    // Registrar nuevo usuario
    public void registerUser(String email, String password, String name, AuthCallback callback) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Guardar nombre en Firestore
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            FirebaseFirestore db = FirebaseFirestore.getInstance();
                            Map<String, Object> userData = new HashMap<>();
                            userData.put("name", name);
                            userData.put("email", email);

                            db.collection("users").document(user.getUid())
                                    .set(userData)
                                    .addOnSuccessListener(aVoid -> callback.onSuccess())
                                    .addOnFailureListener(callback::onError);
                        }
                    } else {
                        callback.onError(task.getException());
                    }
                });
    }
    // Interfaz para callbacks
    public interface AuthCallback {
        void onSuccess();
        void onError(Exception e);
    }
}