package com.alejandrobel.proyecto.flashcards.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.flashcards.adapters.FlashcardAdapter;
import com.alejandrobel.proyecto.flashcards.models.Flashcard;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class FlashcardsActivity extends AppCompatActivity implements FlashcardAdapter.OnAnswerSelectedListener {

    private RecyclerView rvFlashcards;
    private Toolbar toolbar;
    private ArrayList<Flashcard> flashcardsList = new ArrayList<>();
    private FlashcardAdapter adapter;
    private boolean isShowingFront = true;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_cards);

        rvFlashcards = findViewById(R.id.rv_flashcards);
        toolbar = findViewById(R.id.toolbar_flashcards);

        // Toolbar con retroceso
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        // RecyclerView
        adapter = new FlashcardAdapter(flashcardsList, this);
        rvFlashcards.setLayoutManager(new LinearLayoutManager(this));
        rvFlashcards.setAdapter(adapter);

        // Cargar datos de Firestore
        cargarFlashcardsDesdeFirestore();
    }

    private void cargarFlashcardsDesdeFirestore() {
        db.collection("flashcards")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    flashcardsList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Flashcard card = doc.toObject(Flashcard.class);
                        card.setId(doc.getId());
                        flashcardsList.add(card);
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar las tarjetas.", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Fallo al obtener flashcards", e);
                });
    }

    @Override
    public void onAnswerSelected(int position, boolean userAnswer, View cardFront, View cardBack) {
        Flashcard card = flashcardsList.get(position);
        boolean isCorrect = userAnswer == card.getIsTrue();
        card.setMastered(isCorrect);

        if (isCorrect) {
            Toast.makeText(this, "¡Respuesta correcta!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
            flipCard(cardFront, cardBack, position);
        }

        // Guardar el progreso en Firestore
        db.collection("flashcards")
                .document(card.getId())
                .set(card)
                .addOnSuccessListener(aVoid -> Log.d("Firestore", "Flashcard actualizada"))
                .addOnFailureListener(e -> Log.e("Firestore", "Error al guardar", e));
    }

    private void flipCard(View front, View back, int position) {
        if (isShowingFront) {
            front.animate().rotationY(90).setDuration(200).withEndAction(() -> {
                front.setVisibility(View.GONE);
                back.setRotationY(-90);
                back.setVisibility(View.VISIBLE);
                back.animate().rotationY(0).setDuration(200).start();
            }).start();
        } else {
            back.animate().rotationY(90).setDuration(200).withEndAction(() -> {
                back.setVisibility(View.GONE);
                front.setRotationY(-90);
                front.setVisibility(View.VISIBLE);
                front.animate().rotationY(0).setDuration(200).start();
            }).start();
        }
        isShowingFront = !isShowingFront;
    }
}