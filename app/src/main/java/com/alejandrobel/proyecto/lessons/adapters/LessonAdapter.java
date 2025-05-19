package com.alejandrobel.proyecto.lessons.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.alejandrobel.proyecto.R;
import com.alejandrobel.proyecto.lessons.models.Lesson;
import java.util.List;

public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.LessonViewHolder> {

    private final List<Lesson> lessons;
    private final OnLessonClickListener listener;

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
        private final TextView tvTitle, tvDescription;

        public LessonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_lesson_title);
            tvDescription = itemView.findViewById(R.id.tv_lesson_description);
        }

        public void bind(Lesson lesson) {
            tvTitle.setText(lesson.getTitle());
            tvDescription.setText(lesson.getDescription());
            itemView.setOnClickListener(v -> listener.onLessonClick(lesson));
        }
    }

    public interface OnLessonClickListener {
        void onLessonClick(Lesson lesson);
    }
}