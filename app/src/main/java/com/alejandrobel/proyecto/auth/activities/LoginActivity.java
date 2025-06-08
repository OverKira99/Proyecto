package com.alejandrobel.proyecto.auth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.auth.repositories.AuthRepository;
import com.alejandrobel.proyecto.auth.utils.AuthValidator;
import com.alejandrobel.proyecto.main.MainActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException; // Corregido
import com.google.firebase.auth.FirebaseAuth; // Corregido

public class LoginActivity extends AppCompatActivity {

    // Views
    private EditText etEmail, etPassword;
    private Button btnLogin, btnGoogleSignIn;
    private TextView tvForgotPassword, tvRegister;
    // Auth
    private AuthRepository authRepository;
    private FirebaseAuth mAuth;
    private GoogleSignInClient googleSignInClient;
    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();

        authRepository = new AuthRepository();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        btnGoogleSignIn = findViewById(R.id.btn_google_signin);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvRegister = findViewById(R.id.tv_register);

        btnLogin.setOnClickListener(v -> handleEmailLogin());
        btnGoogleSignIn.setOnClickListener(v -> signInWithGoogle());
        tvForgotPassword.setOnClickListener(v -> showForgotPasswordDialog());
        tvRegister.setOnClickListener(v -> navigateToRegister());
    }

    private void handleEmailLogin() {
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (!AuthValidator.isValidEmail(email)) {
            etEmail.setError("Ingresa un email válido");
            return;
        }

        if (!AuthValidator.isValidPassword(password)) {
            etPassword.setError("La contraseña debe tener al menos 6 caracteres");
            return;
        }

        authRepository.loginWithEmail(email, password, new AuthRepository.AuthCallback() {
            @Override
            public void onSuccess() {
                navigateToMain();
            }

            @Override
            public void onError(Exception e) {
                showError("Error al iniciar sesión: " + e.getMessage());
            }
        });
    }

    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void showForgotPasswordDialog() {
        // Implementar lógica de recuperación de contraseña
        Toast.makeText(this, "Funcionalidad en desarrollo", Toast.LENGTH_SHORT).show();
    }

    private void navigateToRegister() {
        startActivity(new Intent(this, RegisterActivity.class));
    }

    private void navigateToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void showError(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            try {
                String idToken = GoogleSignIn.getSignedInAccountFromIntent(data)
                        .getResult(ApiException.class).getIdToken();
                authRepository.loginWithGoogle(idToken, new AuthRepository.AuthCallback() {
                    @Override
                    public void onSuccess() {
                        navigateToMain();
                    }

                    @Override
                    public void onError(Exception e) {
                        showError("Error con Google: " + e.getMessage());
                    }
                });
            } catch (ApiException e) {
                showError("Error con Google Sign-In: " + e.getStatusCode());
            }
        }
    }
}
