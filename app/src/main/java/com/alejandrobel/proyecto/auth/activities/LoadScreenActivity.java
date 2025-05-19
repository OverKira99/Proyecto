package com.alejandrobel.proyecto.auth.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import com.airbnb.lottie.LottieAnimationView;
import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.main.MainActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoadScreenActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3000; // 3 segundos
    private LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_screen);

        // Configurar la animación Lottie
        animationView = findViewById(R.id.animationView);
        setupAnimation();

        // Ocultar ActionBar si existe
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Verificar estado de autenticación después del delay
        new Handler().postDelayed(this::checkAuthState, SPLASH_DELAY);
    }

    private void setupAnimation() {
        // Configuración adicional de la animación si es necesaria
        animationView.setAnimation(R.raw.loading_animation);
        animationView.playAnimation();
        animationView.loop(true);
    }

    private void checkAuthState() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser != null && currentUser.isEmailVerified()) {
            // Usuario logueado y verificado - ir a MainActivity
            navigateToMain();
        } else {
            // Usuario no logueado o no verificado - ir a LoginActivity
            navigateToLogin();
        }
    }

    private void navigateToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    private void navigateToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    @Override
    protected void onDestroy() {
        // Liberar recursos de Lottie
        if (animationView != null) {
            animationView.cancelAnimation();
        }
        super.onDestroy();
    }
}