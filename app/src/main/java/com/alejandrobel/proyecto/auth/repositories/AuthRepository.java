package com.alejandrobel.proyecto.auth.repositories;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FieldValue;
import java.util.HashMap;
import java.util.Map;

public class AuthRepository {
    private final FirebaseAuth mAuth;
    private final FirebaseFirestore db;
    private static final String TAG = "AuthRepository";

    public AuthRepository() {
        this.mAuth = FirebaseAuth.getInstance();
        this.db = FirebaseFirestore.getInstance();
    }

    // Login con Email/Contraseña
    public void loginWithEmail(String email, String password, AuthCallback callback) {
        if (!validateEmail(email)) {
            callback.onError(new IllegalArgumentException("Formato de email inválido"));
            return;
        }

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "loginWithEmail:success");
                        callback.onSuccess();
                    } else {
                        Log.w(TAG, "loginWithEmail:failure", task.getException());
                        callback.onError(translateAuthException(task.getException()));
                    }
                });
    }

    // Login con Google
    public void loginWithGoogle(String idToken, AuthCallback callback) {
        if (idToken == null || idToken.isEmpty()) {
            callback.onError(new IllegalArgumentException("Token de Google inválido"));
            return;
        }

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "loginWithGoogle:success");
                        handleNewGoogleUser(callback);
                    } else {
                        Log.w(TAG, "loginWithGoogle:failure", task.getException());
                        callback.onError(translateAuthException(task.getException()));
                    }
                });
    }

    private void handleNewGoogleUser(AuthCallback callback) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            db.collection("users").document(user.getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful() || !task.getResult().exists()) {
                            saveUserData(user, callback);
                        } else {
                            callback.onSuccess();
                        }
                    });
        }
    }

    // Registrar nuevo usuario - Versión mejorada
    public void registerUser(String email, String password, String name, AuthCallback callback) {
        if (!validateEmail(email)) {
            callback.onError(new IllegalArgumentException("Formato de email inválido"));
            return;
        }

        if (password == null || password.length() < 6) {
            callback.onError(new IllegalArgumentException("La contraseña debe tener al menos 6 caracteres"));
            return;
        }

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            // 1. Enviar email de verificación
                            sendEmailVerification(user);

                            // 2. Guardar datos en Firestore
                            saveUserDataWithRetry(user, name, email, callback);
                        }
                    } else {
                        callback.onError(translateAuthException(task.getException()));
                    }
                });
    }

    private void sendEmailVerification(FirebaseUser user) {
        user.sendEmailVerification()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Email de verificación enviado");
                    }
                });
    }

    private void saveUserDataWithRetry(FirebaseUser user, String name, String email, AuthCallback callback) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name);
        userData.put("email", email);
        userData.put("createdAt", FieldValue.serverTimestamp());
        userData.put("emailVerified", false);

        db.collection("users").document(user.getUid())
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Datos de usuario guardados exitosamente");
                    callback.onSuccess(); // Solo llamamos onSuccess cuando todo está completo
                })
                .addOnFailureListener(e -> {
                    Log.w(TAG, "Error al guardar datos, reintentando...", e);
                    // Reintento después de un breve retraso
                    new android.os.Handler().postDelayed(() ->
                            saveUserDataWithRetry(user, name, email, callback), 1000);
                });
    }

    private void saveUserData(FirebaseUser user, AuthCallback callback) {
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", user.getDisplayName());
        userData.put("email", user.getEmail());
        userData.put("createdAt", FieldValue.serverTimestamp());
        userData.put("isGoogleUser", true);
        userData.put("emailVerified", user.isEmailVerified());

        db.collection("users").document(user.getUid())
                .set(userData)
                .addOnSuccessListener(aVoid -> callback.onSuccess())
                .addOnFailureListener(callback::onError);
    }

    private boolean validateEmail(String email) {
        return email != null && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private Exception translateAuthException(Exception e) {
        if (e instanceof FirebaseAuthInvalidCredentialsException) {
            return new IllegalArgumentException("Credenciales inválidas");
        } else if (e instanceof FirebaseAuthWeakPasswordException) {
            return new IllegalArgumentException("Contraseña demasiado débil");
        } else if (e instanceof FirebaseAuthUserCollisionException) {
            return new IllegalArgumentException("El usuario ya existe");
        }
        return e;
    }

    public interface AuthCallback {
        void onSuccess();
        void onError(Exception e);
    }
}