package com.alejandrobel.proyecto.lessons.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.lessons.adapters.LessonAdapter;
import com.alejandrobel.proyecto.lessons.models.Lesson;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LessonsActivity extends AppCompatActivity implements LessonAdapter.OnLessonClickListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private LessonAdapter adapter;
    private Toolbar toolbar;
    private List<Lesson> lessons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);
        toolbar = findViewById(R.id.toolbar_lessons);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_lessons);


        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        adapter = new LessonAdapter(lessons, this);
        recyclerView.setAdapter(adapter);


        loadSampleLessons();
    }

    private void loadSampleLessons() {
        progressBar.setVisibility(View.VISIBLE);

        // Simular carga de datos
        recyclerView.postDelayed(() -> {
            lessons.clear();

            // Añadir lecciones de ejemplo
            lessons.add(new Lesson(
                    "1",
                    "Fundamentos Conceptuales",
                    "Contexto histórico y filosófico de ambos lenguajes.",
                    "https://www.youtube.com/watch?v=FF9vlX-ia7I",
                    200, //
                    new Date()
            ));

            lessons.add(new Lesson(
                    "2",
                    "Sintaxis Comparada",
                    "Análisis lado a lado de estructuras básicas.",
                    "https://www.youtube.com/watch?v=a0seWcyS624",
                    552,
                    new Date()
            ));
            lessons.add(new Lesson(
                    "3",
                    "Paradigmas de Programación",
                    "Implementación de POO y funcional.",
                    "https://youtu.be/CWzOBeiBiFA?si=W6DNjd-TsCgLae0R",
                    668,
                    new Date()
            ));

            adapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);
        }, 1500); // Retardo simulado de 1.5 segundos
    }

    @Override
    public void onLessonClick(Lesson lesson) {
        Toast.makeText(this, "Seleccionado: " + lesson.getTitle(), Toast.LENGTH_SHORT).show();
        // Aquí puedes implementar la navegación a la pantalla de detalle
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}