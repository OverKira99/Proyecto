package com.alejandrobel.proyecto.lessons.activities;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.alejandrobel.proyecto.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class YouTubePlayerActivity extends AppCompatActivity {

    public static final String EXTRA_VIDEO_ID = "video_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube_player);

        String videoId = getIntent().getStringExtra(EXTRA_VIDEO_ID);

        // Validación adicional
        if (videoId == null || videoId.isEmpty()) {
            Toast.makeText(this, "Error: Video no disponible", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                // Validación adicional antes de reproducir
                try {
                    youTubePlayer.loadVideo(videoId, 0);
                } catch (Exception e) {
                    Toast.makeText(YouTubePlayerActivity.this, "Error al reproducir video", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }
}