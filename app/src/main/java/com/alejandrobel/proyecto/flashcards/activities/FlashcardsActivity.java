package com.alejandrobel.proyecto.flashcards.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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
import java.util.List;
public class FlashcardsActivity extends AppCompatActivity implements FlashcardAdapter.OnAnswerSelectedListener {

    private RecyclerView rvFlashcards;
    private Toolbar toolbar;
    private Spinner spinnerDificultad;

    private ArrayList<Flashcard> flashcardsList = new ArrayList<>();  // Mostradas
    private ArrayList<Flashcard> listaCompleta = new ArrayList<>();   // Todas

    private FlashcardAdapter adapter;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private boolean isShowingFront = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_cards);

        rvFlashcards = findViewById(R.id.rv_flashcards);
        toolbar = findViewById(R.id.toolbar_flashcards);
        spinnerDificultad = findViewById(R.id.spinner_dificultad);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        adapter = new FlashcardAdapter(flashcardsList, this);
        rvFlashcards.setLayoutManager(new LinearLayoutManager(this));
        rvFlashcards.setAdapter(adapter);

        // Configura Spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.niveles_dificultad, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDificultad.setAdapter(spinnerAdapter);

        spinnerDificultad.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String filtro = parent.getItemAtPosition(position).toString();
                filtrarPorDificultad(filtro);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        cargarFlashcardsDesdeFirestore();
    }

    private void cargarFlashcardsDesdeFirestore() {
        db.collection("flashcards")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    flashcardsList.clear();
                    listaCompleta.clear();

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Flashcard card = doc.toObject(Flashcard.class);
                        card.setId(doc.getId());
                        flashcardsList.add(card);
                        listaCompleta.add(card); // ← importante
                    }

                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Error al cargar las tarjetas.", Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Fallo al obtener flashcards", e);
                });
    }

    private void filtrarPorDificultad(String dificultad) {
        if (dificultad.equals("Todos")) {
            flashcardsList.clear();
            flashcardsList.addAll(listaCompleta);
        } else {
            flashcardsList.clear();
            for (Flashcard f : listaCompleta) {
                if (f.getDificultad().equalsIgnoreCase(dificultad)) {
                    flashcardsList.add(f);
                }
            }
        }
        adapter.notifyDataSetChanged();
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