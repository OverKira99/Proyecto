package com.alejandrobel.proyecto.auth.activities;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.auth.repositories.AuthRepository;
import com.alejandrobel.proyecto.auth.utils.AuthValidator;
import com.alejandrobel.proyecto.main.MainActivity;

public class RegisterActivity extends AppCompatActivity {

    // Views
    private EditText etName, etEmail, etPassword, etConfirmPassword;
    private Button btnRegister;

    // Auth
    private AuthRepository authRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializar Firebase Auth
        authRepository = new AuthRepository();

        // Vincular vistas
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etConfirmPassword = findViewById(R.id.et_confirm_password);
        btnRegister = findViewById(R.id.btn_register);

        // Listener del botón
        btnRegister.setOnClickListener(v -> handleRegistration());
    }

    private void handleRegistration() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String confirmPassword = etConfirmPassword.getText().toString().trim();

        // Validaciones
        if (name.isEmpty()) {
            etName.setError("Ingresa tu nombre");
            return;
        }

        if (!AuthValidator.isValidEmail(email)) {
            etEmail.setError("Email inválido");
            return;
        }

        if (!AuthValidator.isValidPassword(password)) {
            etPassword.setError("Mínimo 6 caracteres");
            return;
        }

        if (!AuthValidator.doPasswordsMatch(password, confirmPassword)) {
            etConfirmPassword.setError("Las contraseñas no coinciden");
            return;
        }

        // Registrar usuario
        authRepository.registerUser(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess() {
                Toast.makeText(RegisterActivity.this, "¡Registro exitoso!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(Exception e) {
                Toast.makeText(RegisterActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void navigateToLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}