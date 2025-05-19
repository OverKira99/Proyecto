package com.alejandrobel.proyecto.auth.utils;

import android.text.TextUtils;
import android.util.Patterns;

import java.util.regex.Pattern;

public class AuthValidator {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
            Pattern.CASE_INSENSITIVE
    );

    public static boolean isValidEmail(String email) {
        if (TextUtils.isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }
    // Validar formato de email


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