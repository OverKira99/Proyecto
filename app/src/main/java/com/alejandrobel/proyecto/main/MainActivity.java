package com.alejandrobel.proyecto.main;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.auth.activities.LoginActivity;
import com.alejandrobel.proyecto.flashcards.activities.FlashcardsActivity;
import com.alejandrobel.proyecto.lessons.activities.LessonsActivity;
import com.alejandrobel.proyecto.profile.activities.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        tvWelcome = findViewById(R.id.tv_welcome);

        findViewById(R.id.card_study_cards).setOnClickListener(v -> {
            Intent intent = new Intent(this, FlashcardsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.card_lessons).setOnClickListener(v -> {
            Intent intent = new Intent(this, LessonsActivity.class);
            startActivity(intent);
        });

        findViewById(R.id.card_profile).setOnClickListener(v -> {
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
        });

        loadUserData();
    }

    private void loadUserData() {
        if (mAuth.getCurrentUser() != null) {
            String userId = mAuth.getCurrentUser().getUid();
            db.collection("users").document(userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String name = document.getString("name");
                                tvWelcome.setText("¡Bienvenido, " + name + "!");
                            }
                        }
                    });
        }
    }


}