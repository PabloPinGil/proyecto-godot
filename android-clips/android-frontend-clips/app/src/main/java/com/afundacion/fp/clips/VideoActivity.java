package com.afundacion.fp.clips;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {
    public static final String INTENT_CLIP_ID = "CLIP_ID";
    public static final String INTENT_CLIP_URL = "CLIP_URL";
    private VideoView videoView;
    private Activity activity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        Intent intent = getIntent(); // Con esto accedemos al Intent que se usó para iniciar la actividad
        int clipId = intent.getIntExtra(VideoActivity.INTENT_CLIP_ID, -1);
        String clipUrl = intent.getStringExtra(VideoActivity.INTENT_CLIP_URL);

        Toast.makeText(this, "clipID: " + clipId, Toast.LENGTH_LONG).show();

        videoView = findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse(clipUrl));
        videoView.start();

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                activity.finish();
            }
        });

    }
}
