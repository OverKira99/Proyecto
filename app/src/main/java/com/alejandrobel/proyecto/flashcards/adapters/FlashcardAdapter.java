package com.alejandrobel.proyecto.flashcards.adapters;

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

public class FlashcardAdapter extends RecyclerView.Adapter<FlashcardAdapter.ViewHolder> {

    private final List<Flashcard> flashcards;
    private final OnAnswerSelectedListener listener;

    public interface OnAnswerSelectedListener {
        void onAnswerSelected(int position, boolean userAnswer, View cardFront, View cardBack);
    }

    public FlashcardAdapter(List<Flashcard> flashcards, OnAnswerSelectedListener listener) {
        this.flashcards = flashcards;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FlashcardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FlashcardAdapter.ViewHolder holder, int position) {
        Flashcard flashcard = flashcards.get(position);

        holder.tvTitle.setText(flashcard.getTitulo());
        holder.tvDescription.setText(flashcard.getDescripcion());
        holder.tvQuestion.setText(flashcard.getPregunta());
        holder.tvDifficulty.setText(flashcard.getDificultad());
        holder.tvFeedback.setText(flashcard.getFeedback() != null ? flashcard.getFeedback() : "Respuesta incorrecta.");
        holder.tvFeedback.setVisibility(View.GONE);

        holder.rbTrue.setChecked(false);
        holder.rbFalse.setChecked(false);

        holder.rbGroup.setOnCheckedChangeListener((group, checkedId) -> {
            boolean userAnswer = checkedId == R.id.rb_true;
            listener.onAnswerSelected(holder.getAdapterPosition(), userAnswer, holder.tvQuestion, holder.tvFeedback);
        });
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDescription, tvQuestion, tvFeedback, tvDifficulty;
        RadioButton rbTrue, rbFalse;
        RadioGroup rbGroup;
        ImageView ivStar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTitle = itemView.findViewById(R.id.tv_lesson_title);
            tvDescription = itemView.findViewById(R.id.tv_description);
            tvQuestion = itemView.findViewById(R.id.tv_question);
            tvFeedback = itemView.findViewById(R.id.tv_feedback);
            tvDifficulty = itemView.findViewById(R.id.tv_difficulty);
            ivStar = itemView.findViewById(R.id.iv_difficulty_star);
            rbTrue = itemView.findViewById(R.id.rb_true);
            rbFalse = itemView.findViewById(R.id.rb_false);
            rbGroup = itemView.findViewById(R.id.rb_answer);
        }
    }
}