package com.alejandrobel.proyecto.flashcards.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.flashcards.models.Flashcard;

public class FlashcardDetailActivity extends AppCompatActivity {
    private TextView tvQuestion, tvAnswer, tvJavaCode, tvPythonCode, tvConclusion;
    private Button btnShowAnswer;
    private boolean answerShown = false;
    private Flashcard currentFlashcard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard_detail);

        // Inicializar vistas
        tvQuestion = findViewById(R.id.tv_question_detail);
        tvAnswer = findViewById(R.id.tv_answer_detail);
        tvJavaCode = findViewById(R.id.tv_java_code);
        tvPythonCode = findViewById(R.id.tv_python_code);
        tvConclusion = findViewById(R.id.tv_conclusion);
        btnShowAnswer = findViewById(R.id.btn_show_answer);

        // Obtener flashcard del intent
        currentFlashcard = (Flashcard) getIntent().getSerializableExtra("flashcard");

        if (currentFlashcard != null) {
            tvQuestion.setText(currentFlashcard.getQuestion());

            btnShowAnswer.setOnClickListener(v -> {
                if (!answerShown) {
                    showAnswer();
                } else {
                    finish(); // O implementar lógica para siguiente flashcard
                }
            });
        }
    }

    private void showAnswer() {
        tvAnswer.setText(currentFlashcard.getAnswer());
        tvAnswer.setVisibility(View.VISIBLE);

        if (!currentFlashcard.getCodeExampleJava().isEmpty()) {
            findViewById(R.id.tv_java_label).setVisibility(View.VISIBLE);
            tvJavaCode.setText(currentFlashcard.getCodeExampleJava());
            tvJavaCode.setVisibility(View.VISIBLE);
        }

        if (!currentFlashcard.getCodeExamplePython().isEmpty()) {
            findViewById(R.id.tv_python_label).setVisibility(View.VISIBLE);
            tvPythonCode.setText(currentFlashcard.getCodeExamplePython());
            tvPythonCode.setVisibility(View.VISIBLE);
        }

        tvConclusion.setText(currentFlashcard.getConclusion());
        tvConclusion.setVisibility(View.VISIBLE);

        btnShowAnswer.setText("Siguiente Flashcard");
        answerShown = true;
    }
}