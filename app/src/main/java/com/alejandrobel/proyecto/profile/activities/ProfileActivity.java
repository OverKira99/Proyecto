package com.alejandrobel.proyecto.profile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.auth.activities.LoginActivity;
import com.alejandrobel.proyecto.flashcards.models.Flashcard;
import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ProfileActivity extends AppCompatActivity {

    private ImageView ivProfile;
    private TextView tvName, tvEmail, tvProgreso, tvResumen;
    private ProgressBar progressBar;
    private Button btnLogout;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.top_app_bar_profile);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        ivProfile = findViewById(R.id.iv_profile);
        tvName = findViewById(R.id.tv_name_profile);
        tvEmail = findViewById(R.id.tv_email_profile);
        tvProgreso = findViewById(R.id.tv_progreso);
        tvResumen = findViewById(R.id.tv_resumen_dificultad);
        progressBar = findViewById(R.id.progress_bar);
        btnLogout = findViewById(R.id.btn_logout);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            tvName.setText(user.getDisplayName());
            tvEmail.setText(user.getEmail());

            if (user.getPhotoUrl() != null) {
                Glide.with(this).load(user.getPhotoUrl()).into(ivProfile);
            }

            cargarProgresoFlashcards();
        }

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });
    }

    private void cargarProgresoFlashcards() {
        db.collection("flashcards")
                .get()
                .addOnSuccessListener(query -> {
                    int total = 0;
                    int completadas = 0;
                    int baja = 0, media = 0, alta = 0;

                    for (QueryDocumentSnapshot doc : query) {
                        Flashcard f = doc.toObject(Flashcard.class);
                        total++;
                        if (f.isMastered()) completadas++;

                        String dificultad = f.getDificultad().toLowerCase();
                        switch (dificultad) {
                            case "baja": baja++; break;
                            case "media": media++; break;
                            case "alta": alta++; break;
                        }
                    }

                    int porcentaje = (total == 0) ? 0 : (completadas * 100 / total);
                    progressBar.setProgress(porcentaje);
                    tvProgreso.setText("Progreso: " + completadas + "/" + total + " (" + porcentaje + "%)");

                    String resumen = "Baja: " + baja + "  •  Media: " + media + "  •  Alta: " + alta;
                    tvResumen.setText(resumen);
                });
    }
}