package com.alejandrobel.proyecto.auth.utils;

import android.util.Patterns;

public class AuthValidator {

    // Validar formato de email
    public static boolean isValidEmail(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Validar contraseña (mínimo 6 caracteres)
    public static boolean isValidPassword(String password) {
        return password != null && password.length() >= 6;
    }

    // Validar que dos contraseñas coincidan (para registro)
    public static boolean doPasswordsMatch(String password, String confirmPassword) {
        return password.equals(confirmPassword);
    }
    public static boolean isPasswordStrong(String password) {
        return password.matches("^(?=.*[A-Z])(?=.*\\d).{6,}$"); // Al menos 1 mayúscula y 1 número
    }
}