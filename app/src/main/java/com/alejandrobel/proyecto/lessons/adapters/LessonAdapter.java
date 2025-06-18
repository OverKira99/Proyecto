package com.alejandrobel.proyecto.lessons.adapters;

import android.content.Intent;
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
import com.alejandrobel.proyecto.lessons.activities.YouTubePlayerActivity;
import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        private final ImageView ivPlay, ivVideo_thumbnail;
        private final TextView tvTitle, tvDescription, tvDuration;
        private MediaController mediaController;
        private Lesson currentLesson;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            ivVideo_thumbnail = itemView.findViewById(R.id.video_thumbnail);
            ivPlay = itemView.findViewById(R.id.iv_play);
            tvTitle = itemView.findViewById(R.id.tv_lesson_title);
            tvDescription = itemView.findViewById(R.id.tv_lesson_description);
            tvDuration = itemView.findViewById(R.id.tv_duration);

            // Solo usamos VideoView para mostrar el primer frame como miniatura (opcional)
            mediaController = new MediaController(itemView.getContext());
            mediaController.setAnchorView(ivVideo_thumbnail);


            // Abrir reproductor de YouTube al hacer clic
            itemView.setOnClickListener(v -> {
                if (currentLesson != null) {
                    String videoId = extractYouTubeVideoId(currentLesson.getVideoUrl());

                    Intent intent = new Intent(itemView.getContext(), YouTubePlayerActivity.class);
                    intent.putExtra(YouTubePlayerActivity.EXTRA_VIDEO_ID, videoId);
                    itemView.getContext().startActivity(intent);
                }
            });



        }

        public void bind(Lesson lesson) {
            currentLesson = lesson;

            tvTitle.setText(lesson.getTitle());
            tvDescription.setText(lesson.getDescription());
            tvDuration.setText(formatDuration(lesson.getDuration()));


            String videoId = extractYouTubeVideoId(lesson.getVideoUrl());
            String thumbnailUrl = "https://img.youtube.com/vi/" + videoId + "/0.jpg";


            Glide.with(itemView.getContext())
                    .load(thumbnailUrl)
                    .into((ImageView) itemView.findViewById(R.id.video_thumbnail));

            ivPlay.setVisibility(View.VISIBLE);
        }

        private String formatDuration(int seconds) {
            int minutes = seconds / 60;
            int remainingSeconds = seconds % 60;
            return String.format(Locale.getDefault(), "%d:%02d", minutes, remainingSeconds);
        }

        private String extractYouTubeVideoId(String url) {
            if (url == null || url.trim().isEmpty()) return "";

            String videoId = "";
            String pattern = "(?<=watch\\?v=|/videos/|embed\\/|youtu.be\\/|\\/v\\/|\\/e\\/|watch\\?v%3D|watch\\?feature=player_embedded&v=|%2Fvideos%2F|embed%\\?video_id=|\\?v=|\\&v=|youtu.be\\/|watch\\?v=|\\/v\\/|watch\\?v=|\\/embed\\/)[^#\\&\\?\\n]*";

            Pattern compiledPattern = Pattern.compile(pattern);
            Matcher matcher = compiledPattern.matcher(url);

            if(matcher.find()){
                videoId = matcher.group();
            }

            return videoId;
        }
    }
}