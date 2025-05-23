package com.alejandrobel.proyecto.flashcards.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.flashcards.models.Flashcard;

import java.util.List;

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.FlashcardViewHolder> {

    private List<Flashcard> flashcards;
    private OnAnswerSelectedListener answerListener;

    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int position, boolean answer);
    }

    public FlashcardAdapter(List<Flashcard> listSet, OnAnswerSelectedListener listener) {
        this.flashcards = listSet;
        this.answerListener = listener;
    }

    public class FlashcardViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLessonTitle;
        private TextView tvDescription;
        private TextView tvQuestion;
        private RadioGroup rbAnswer;
        private RadioButton rbTrue;
        private RadioButton rbFalse;
        private TextView tvDifficulty;
        private ImageView ivDifficultyStar;

        public FlashcardViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLessonTitle = itemView.findViewById(R.id.tv_lesson_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvQuestion = itemView.findViewById(R.id.tv_question);
            rbAnswer = itemView.findViewById(R.id.rb_answer);
            rbTrue = itemView.findViewById(R.id.rb_true);
            rbFalse = itemView.findViewById(R.id.rb_false);
            tvDifficulty = itemView.findViewById(R.id.tv_difficulty);
            ivDifficultyStar = itemView.findViewById(R.id.iv_difficulty_star);

            rbAnswer.setOnCheckedChangeListener((group, checkedId) -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && answerListener != null) {
                    boolean answer = (checkedId == R.id.rb_true);
                    answerListener.onAnswerSelected(position, answer);
                }
            });
        }

        public void bind(Flashcard flashcard) {
            tvLessonTitle.setText(flashcard.getTitle());
            tvDescription.setText(flashcard.getDescription());
            tvQuestion.setText(flashcard.getQuestion());

            // Configurar dificultad
            tvDifficulty.setText(flashcard.getDifficulty());
            ivDifficultyStar.setColorFilter(getDifficultyColor(flashcard.getDifficulty()));

            // Configurar respuesta seleccionada (si existe)
            rbAnswer.setOnCheckedChangeListener(null); // Desactivar temporalmente el listener
            if (flashcard.getUserAnswer() != null) {
                if (flashcard.getUserAnswer()) {
                    rbTrue.setChecked(true);
                } else {
                    rbFalse.setChecked(true);
                }
            } else {
                rbAnswer.clearCheck();
            }
            rbAnswer.setOnCheckedChangeListener((group, checkedId) -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION && answerListener != null) {
                    boolean answer = (checkedId == R.id.rb_true);
                    answerListener.onAnswerSelected(position, answer);
                }
            });
        }

        private int getDifficultyColor(String difficulty) {
            switch (difficulty.toLowerCase()) {
                case "fácil": return Color.GREEN;
                case "media": return Color.YELLOW;
                case "difícil": return Color.RED;
                default: return Color.YELLOW;
            }
        }
    }

    @NonNull
    @Override
    public FlashcardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard, parent, false);
        return new FlashcardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardViewHolder holder, int position) {
        holder.bind(flashcards.get(position));
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }
}