package com.alejandrobel.proyecto.lessons.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.lessons.adapters.LessonAdapter;
import com.alejandrobel.proyecto.lessons.models.Lesson;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class LessonsActivity extends AppCompatActivity implements LessonAdapter.OnLessonClickListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_lessons);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar Firestore
        db = FirebaseFirestore.getInstance();

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recycler_lessons);
        progressBar = findViewById(R.id.progress_bar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Cargar lecciones
        loadLessons();
    }

    private void loadLessons() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("lessons")
                .orderBy("createdAt", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        List<Lesson> lessons = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Lesson lesson = doc.toObject(Lesson.class);
                            lesson.setId(doc.getId());
                            lessons.add(lesson);
                        }
                        updateLessonsList(lessons);
                    } else {
                        Toast.makeText(this, "Error cargando lecciones: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateLessonsList(List<Lesson> lessons) {
        LessonAdapter adapter = new LessonAdapter(lessons, this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onLessonClick(Lesson lesson) {
        // Navegar a LessonDetailActivity (si existe)
        Toast.makeText(this, "Lección seleccionada: " + lesson.getTitle(), Toast.LENGTH_SHORT).show();
    }
}