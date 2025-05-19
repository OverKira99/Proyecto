package com.alejandrobel.proyecto.flashcards.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.flashcards.adapters.FlashcardsAdapter;
import com.alejandrobel.proyecto.flashcards.models.Flashcard;
import com.alejandrobel.proyecto.utils.FirebaseDataInitializer;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FlashcardsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FlashcardsAdapter adapter;
    private List<Flashcard> flashcards = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_cards);

        // Configurar ActionBar con botón de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Mis Flashcards");
        }

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new FlashcardsAdapter(flashcards, this);
        recyclerView.setAdapter(adapter);

        // Inicializar datos si es necesario
        initializeDataIfNeeded();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initializeDataIfNeeded() {
        new FirebaseDataInitializer(this).initializeFlashcardsIfNeeded()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        loadFlashcards();
                    } else {
                        Toast.makeText(this, "Error inicializando datos", Toast.LENGTH_SHORT).show();
                        Log.e("FlashcardsActivity", "Error en inicialización", task.getException());
                    }
                });
    }

    private void loadFlashcards() {
        db.collection("flashcards")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Flashcard> tempList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                Flashcard flashcard = document.toObject(Flashcard.class);
                                flashcard.setId(document.getId());
                                tempList.add(flashcard);
                            } catch (Exception e) {
                                Log.e("FlashcardsActivity", "Error parsing flashcard", e);
                            }
                        }
                        adapter.updateData(tempList);

                        if (tempList.isEmpty()) {
                            Toast.makeText(this, "No hay flashcards disponibles", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.e("Firestore", "Error getting documents: ", task.getException());
                        Toast.makeText(this, "Error al cargar flashcards", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}