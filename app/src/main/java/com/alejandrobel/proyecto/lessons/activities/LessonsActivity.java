package com.alejandrobel.proyecto.lessons.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
    private List<Lesson> lessons = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        // Configurar flecha de retroceso
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Inicializar vistas
        progressBar = findViewById(R.id.progress_bar);
        recyclerView = findViewById(R.id.recycler_lessons);

        // Configurar RecyclerView
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);

        adapter = new LessonAdapter(lessons, this);
        recyclerView.setAdapter(adapter);

        // Cargar datos de ejemplo
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
                    "https://youtu.be/FF9vlX-ia7I",
                    300, // 5 minutos
                    new Date()
            ));

            lessons.add(new Lesson(
                    "2",
                    "Sintaxis Comparada",
                    "Análisis lado a lado de estructuras básicas.",
                    "https://ejemplo.com/video2.mp4",
                    450, // 7.5 minutos
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