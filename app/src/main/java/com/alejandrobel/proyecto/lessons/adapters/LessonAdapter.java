package com.alejandrobel.proyecto.lessons.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.lessons.models.Lesson;

import java.util.List;
import java.util.Locale;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private final List<Lesson> lessons;
    private final OnLessonClickListener listener;

    public interface OnLessonClickListener {
        void onLessonClick(Lesson lesson);
    }

    public LessonAdapter(List<Lesson> lessons, OnLessonClickListener listener) {
        this.lessons = lessons;
        this.listener = listener;
    }

    @NonNull
    @Override
    public LessonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson, parent, false);
        return new LessonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.bind(lesson);
    }

    @Override
    public int getItemCount() {
        return lessons.size();
    }

    class LessonViewHolder extends RecyclerView.ViewHolder {
        private final VideoView videoPreview;
        private final ImageView ivPlay;
        private final TextView tvTitle, tvDescription, tvDuration;
        private MediaController mediaController;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            videoPreview = itemView.findViewById(R.id.video_preview);
            ivPlay = itemView.findViewById(R.id.iv_play);
            tvTitle = itemView.findViewById(R.id.tv_lesson_title);
            tvDescription = itemView.findViewById(R.id.tv_lesson_description);
            tvDuration = itemView.findViewById(R.id.tv_duration);

            // Configurar MediaController
            mediaController = new MediaController(itemView.getContext());
            mediaController.setAnchorView(videoPreview);
            videoPreview.setMediaController(mediaController);

            // Manejar clics en el ícono de play
            ivPlay.setOnClickListener(v -> {
                if (!videoPreview.isPlaying()) {
                    videoPreview.start();
                    ivPlay.setVisibility(View.GONE);
                }
            });

            // Restaurar ícono de play cuando el video termina
            videoPreview.setOnCompletionListener(mp -> ivPlay.setVisibility(View.VISIBLE));
        }

        public void bind(Lesson lesson) {
            tvTitle.setText(lesson.getTitle());
            tvDescription.setText(lesson.getDescription());
            tvDuration.setText(formatDuration(lesson.getDuration()));

            // Configurar video (solo precarga, no autoplay)
            videoPreview.setVideoPath(lesson.getVideoUrl());
            videoPreview.seekTo(1); // Mostrar primer frame como preview

            itemView.setOnClickListener(v -> listener.onLessonClick(lesson));
        }

        private String formatDuration(int seconds) {
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;
            return String.format(Locale.getDefault(), "%d:%02d", minutes, remainingSeconds);
        }
    }
}