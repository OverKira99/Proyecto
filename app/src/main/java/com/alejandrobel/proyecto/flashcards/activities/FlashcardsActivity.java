package com.alejandrobel.proyecto.flashcards.activities;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.flashcards.adapters.FlashcardAdapter;
import com.alejandrobel.proyecto.flashcards.models.Flashcard;
import java.util.ArrayList;

public class FlashcardsActivity extends AppCompatActivity implements FlashcardAdapter.OnAnswerSelectedListener {

    private RecyclerView rvFlashcards;
    private Toolbar toolbar;
    private ArrayList<Flashcard> flashcardsList = new ArrayList<>();
    private FlashcardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_cards);

        // Inicializar vistas
        rvFlashcards = findViewById(R.id.rv_flashcards);
        toolbar = findViewById(R.id.toolbar_flashcards);

        // Configurar toolbar
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Configurar RecyclerView
        adapter = new FlashcardAdapter(flashcardsList, this);
        rvFlashcards.setAdapter(adapter);
        rvFlashcards.setLayoutManager(new LinearLayoutManager(this));

        // Cargar datos de ejemplo
        loadSampleFlashcards();
    }

    private void loadSampleFlashcards() {
        flashcardsList.add(new Flashcard(
                "Java vs Python",
                "Java es un lenguaje orientado a objetos con sintaxis estricta que requiere definición explícita de tipos de datos.",
                "Java es recomendado para principiantes por su sintaxis flexible y mínima configuración.",
                false,
                null,  // userAnswer inicialmente null
                "Media"
        ));

        flashcardsList.add(new Flashcard(
                "Programación Funcional",
                "La programación funcional se basa en el concepto de funciones matemáticas puras.",
                "En programación funcional, las variables son inmutables por defecto.",
                true,
                null,  // userAnswer inicialmente null
                "Difícil"
        ));

        adapter.notifyDataSetChanged();
    }

    @Override
    public void onAnswerSelected(int position, boolean answer) {
        flashcardsList.get(position).setUserAnswer(answer);

        boolean isCorrect = flashcardsList.get(position).isCorrectAnswer();
        if (answer == isCorrect) {
            Toast.makeText(this, "¡Respuesta correcta!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Respuesta incorrecta", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}