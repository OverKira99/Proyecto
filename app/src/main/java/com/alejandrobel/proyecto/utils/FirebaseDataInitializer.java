package com.alejandrobel.proyecto.utils;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.alejandrobel.proyecto.flashcards.models.Flashcard;
import com.alejandrobel.proyecto.lessons.models.Lesson;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FirebaseDataInitializer {
    private static final String TAG = "FirebaseDataInitializer";
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final SharedPreferences sharedPreferences;

    public FirebaseDataInitializer(Context context) {
        this.sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE);
    }

    // Cambiado para retornar Task<Void>
    public Task<Void> initializeFlashcardsIfNeeded() {
        // Verificar si ya se inicializó en este dispositivo
        if (sharedPreferences.getBoolean("data_initialized", false)) {
            Log.d(TAG, "Los datos ya fueron inicializados previamente en este dispositivo");
            return Tasks.forResult(null);
        }

        // Retornar la cadena completa de Tasks
        return db.collection("flashcards").limit(1).get()
                .continueWithTask(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot result = task.getResult();
                        if (result != null && result.isEmpty()) {
                            return initializeFlashcardsData();
                        } else {
                            Log.d(TAG, "Los datos ya existen en Firestore");
                            markDataAsInitialized();
                            return Tasks.forResult(null);
                        }
                    } else {
                        Log.e(TAG, "Error verificando datos existentes", task.getException());
                        return Tasks.forException(task.getException());
                    }
                });
    }

    // Cambiado para retornar Task<Void>
    private Task<Void> initializeFlashcardsData() {
        List<Flashcard> defaultFlashcards = createDefaultFlashcards();
        WriteBatch batch = db.batch();

        for (Flashcard flashcard : defaultFlashcards) {
            DocumentReference docRef = db.collection("flashcards").document();
            flashcard.setId(docRef.getId());
            batch.set(docRef, flashcard);
        }

        return batch.commit()
                .addOnSuccessListener(aVoid -> {
                    Log.d(TAG, "Batch de flashcards insertado exitosamente");
                    markDataAsInitialized();
                });
    }

    private void markDataAsInitialized() {
        sharedPreferences.edit()
                .putBoolean("data_initialized", true)
                .apply();
    }

    private List<Flashcard> createDefaultFlashcards() {
        List<Flashcard> flashcards = new ArrayList<>();

        // Flashcard 1 - Android Basics
        Flashcard fc1 = createFlashcard(
                "¿Qué es una Activity en Android?",
                "Componente fundamental que representa una pantalla en la aplicación",
                "Android",
                "Media",
                "public class MainActivity extends AppCompatActivity {\n  @Override\n  protected void onCreate(Bundle savedInstanceState) {\n    super.onCreate(savedInstanceState);\n    setContentView(R.layout.activity_main);\n  }\n}",
                "# No aplica para Python",
                "Las Activities gestionan la UI y la interacción del usuario",
                Arrays.asList("android", "ui", "básico")
        );

        // Flashcard 2 - ViewBinding
        Flashcard fc2 = createFlashcard(
                "¿Qué ventajas tiene ViewBinding?",
                "Type safety, null safety y código más limpio",
                "Android",
                "Fácil",
                "// Ejemplo:\nActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());\nsetContentView(binding.getRoot());\nbinding.textView.setText(\"Hola\");",
                "# No aplica",
                "ViewBinding reemplaza ventajosamente a findViewById",
                Arrays.asList("android", "ui", "moderno")
        );

        // Flashcard 3 - Corrutinas
        Flashcard fc3 = createFlashcard(
                "¿Qué son las corrutinas en Kotlin?",
                "Patrón para programación asíncrona más legible que callbacks",
                "Kotlin",
                "Difícil",
                "// Equivalente en Java sería RxJava o AsyncTask\nviewModelScope.launch {\n  val data = repository.fetchData()\n  updateUI(data)\n}",
                "# Similar a asyncio en Python\nasync def main():\n    await asyncio.sleep(1)\n    print('Hello')",
                "Las corrutinas simplifican el código asíncrono",
                Arrays.asList("kotlin", "asincronía", "avanzado")
        );

        flashcards.add(fc1);
        flashcards.add(fc2);
        flashcards.add(fc3);

        return flashcards;
    }

    private Flashcard createFlashcard(String question, String answer, String category,
                                      String difficulty, String javaCode, String pythonCode,
                                      String conclusion, List<String> tags) {
        Flashcard flashcard = new Flashcard();
        flashcard.setQuestion(question);
        flashcard.setAnswer(answer);
        flashcard.setCategory(category);
        flashcard.setDifficulty(difficulty);
        flashcard.setCodeExampleJava(javaCode);
        flashcard.setCodeExamplePython(pythonCode);
        flashcard.setConclusion(conclusion);
        flashcard.setTags(tags);
        flashcard.setCreatedAt(new Date());
        flashcard.setUpdatedAt(new Date());
        return flashcard;
    }

    public Task<Void> initializeLessonsIfNeeded() {
        if (sharedPreferences.getBoolean("lessons_initialized", false)) {
            return Tasks.forResult(null); // Ya inicializado
        }

        return db.collection("lessons").limit(1).get()
                .continueWithTask(task -> {
                    if (task.isSuccessful() && task.getResult().isEmpty()) {
                        return initializeDefaultLessons(); // Crear lecciones iniciales
                    }
                    return Tasks.forResult(null);
                });
    }

    private Task<Void> initializeDefaultLessons() {
        List<Lesson> defaultLessons = new ArrayList<>();

        // Lección 1
        Lesson lesson1 = new Lesson();
        lesson1.setTitle("Android Básico");
        lesson1.setDescription("Activities, Layouts y Views");

        // Lección 2
        Lesson lesson2 = new Lesson();
        lesson2.setTitle("Kotlin Avanzado");
        lesson2.setDescription("Corrutinas y Flow");

        // Batch de escritura
        WriteBatch batch = db.batch();
        defaultLessons.forEach(lesson -> {
            DocumentReference docRef = db.collection("lessons").document();
            batch.set(docRef, lesson);
        });

        return batch.commit()
                .addOnSuccessListener(aVoid -> {
                    sharedPreferences.edit().putBoolean("lessons_initialized", true).apply();
                });
    }
}