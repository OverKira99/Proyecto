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

    // Navegación
    public void navigateToFlashcards(View view) {
        startActivity(new Intent(this, FlashcardsActivity.class));
    }

    public void navigateToLessons(View view) {
        startActivity(new Intent(this, LessonsActivity.class));
    }

    public void navigateToProfile(View view) {
        startActivity(new Intent(this, ProfileActivity.class));
    }

    public void logout(View view) {
        mAuth.signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}