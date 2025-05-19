package com.alejandrobel.proyecto.flashcards.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.flashcards.activities.FlashcardDetailActivity;
import com.alejandrobel.proyecto.flashcards.models.Flashcard;

import java.util.ArrayList;
import java.util.List;

public class FlashcardsAdapter extends RecyclerView.Adapter<FlashcardsAdapter.ViewHolder> {
    private List<Flashcard> flashcards;
    private final Context context;

    public FlashcardsAdapter(List<Flashcard> flashcards, Context context) {
        this.flashcards = flashcards != null ? flashcards : new ArrayList<>();
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_flashcard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Flashcard flashcard = flashcards.get(position);

        // Validación de null para todos los campos
        holder.tvQuestion.setText(flashcard.getQuestion() != null ?
                flashcard.getQuestion() : "Pregunta no disponible");

        holder.tvCategory.setText(flashcard.getCategory() != null ?
                flashcard.getCategory() : "Sin categoría");

        String difficulty = flashcard.getDifficulty() != null ?
                flashcard.getDifficulty() : "Desconocida";
        holder.tvDifficulty.setText(difficulty);

        // Manejo seguro del color
        try {
            holder.tvDifficulty.setTextColor(
                    ContextCompat.getColor(context, getDifficultyColor(difficulty))
            );
        } catch (Resources.NotFoundException e) {
            holder.tvDifficulty.setTextColor(
                    ContextCompat.getColor(context, R.color.gray)
            );
        }

        // Configurar click listener
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FlashcardDetailActivity.class);
            intent.putExtra("flashcard", flashcard);
            context.startActivity(intent);
        });

        // Animación mejorada
        if (!holder.animated) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.slide_in);
            animation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {}

                @Override
                public void onAnimationEnd(Animation animation) {
                    holder.itemView.clearAnimation();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {}
            });
            holder.itemView.startAnimation(animation);
            holder.animated = true;
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.itemView.clearAnimation();
        holder.animated = false; // Resetear flag de animación
    }

    private int getDifficultyColor(String difficulty) {
        if (difficulty == null) return R.color.gray;

        switch (difficulty.toLowerCase()) {
            case "fácil": return R.color.green;
            case "media": return R.color.orange;
            case "difícil": return R.color.red;
            default: return R.color.gray;
        }
    }

    @Override
    public int getItemCount() {
        return flashcards.size();
    }

    public void updateData(List<Flashcard> newFlashcards) {
        this.flashcards = newFlashcards != null ? newFlashcards : new ArrayList<>();
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView tvQuestion;
        final TextView tvCategory;
        final TextView tvDifficulty;
        boolean animated = false;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvQuestion = itemView.findViewById(R.id.tv_question);
            tvCategory = itemView.findViewById(R.id.tv_category);
            tvDifficulty = itemView.findViewById(R.id.tv_difficulty);
        }
    }
}