package com.alejandrobel.proyecto.auth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.auth.repositories.AuthRepository;
import com.alejandrobel.proyecto.auth.utils.AuthValidator;
import com.alejandrobel.proyecto.main.MainActivity;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";

    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;
    private ProgressBar progressBar;
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);
        progressBar = findViewById(R.id.progressBar);

        // Configurar AuthRepository
        authRepository = new AuthRepository();
        // Configurar listeners
        setupListeners();
    }

    private void setupListeners() {
        btnRegister.setOnClickListener(v -> handleRegistration());
    }

    private void handleRegistration() {
        // Obtener y limpiar los valores
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validar campos
        if (!validateFields(name, email, password, confirmPassword)) {
            return;
        }

        // Registrar usuario
        registerNewUser(name, email, password);
    }

    private boolean validateFields(String name, String email, String password, String confirmPassword) {
        boolean isValid = true;

        if (TextUtils.isEmpty(name)) {
            etName.setError("Ingresa tu nombre completo");
            etName.requestFocus();
            isValid = false;
        }

        if (!AuthValidator.isValidEmail(email)) {
            etEmail.setError("Ingresa un correo electrónico válido");
            etEmail.requestFocus();
            isValid = false;
        }

        if (!AuthValidator.isValidPassword(password)) {
            etPassword.setError("La contraseña debe tener al menos 6 caracteres");
            etPassword.requestFocus();
            isValid = false;
        }

        if (!AuthValidator.doPasswordsMatch(password, confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            etConfirmPassword.requestFocus();
            isValid = false;
        }

        return isValid;
    }

    private void registerNewUser(String name, String email, String password) {
        showProgress(true);

        authRepository.registerUser(email, password, name, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess() {
                showProgress(false);
                Toast.makeText(RegisterActivity.this,
                        "¡Registro exitoso! Por favor verifica tu email",
                        Toast.LENGTH_LONG).show();
                navigateToMain();
            }

            @Override
            public void onError(Exception e) {
                showProgress(false);
                handleRegistrationError(e);
                Log.e(TAG, "Error en registro: " + e.getMessage());
            }
        });
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        btnRegister.setEnabled(!show);
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finishAffinity(); // Cierra todas las actividades anteriores
    }

    private void handleRegistrationError(Exception e) {
        if (e instanceof FirebaseAuthUserCollisionException) {
            etEmail.setError("Este email ya está registrado");
            etEmail.requestFocus();
        } else if (e instanceof FirebaseAuthInvalidCredentialsException) {
            etEmail.setError("Formato de email inválido");
            etEmail.requestFocus();
        } else if (e instanceof FirebaseAuthWeakPasswordException) {
            etPassword.setError("Contraseña demasiado débil (mínimo 6 caracteres)");
            etPassword.requestFocus();
        } else {
            Toast.makeText(this,
                    "Error en el registro: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    public void navigateToLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}