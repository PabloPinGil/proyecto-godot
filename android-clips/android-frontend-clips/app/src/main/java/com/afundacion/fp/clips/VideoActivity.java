package com.afundacion.fp.clips;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Toast;
import android.widget.VideoView;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

public class VideoActivity extends AppCompatActivity {
    public static final String INTENT_CLIP_ID = "CLIP_ID";
    public static final String INTENT_CLIP_URL = "CLIP_URL";
    private VideoView videoView;
    private Activity activity = this;
    private Context context = this;

    private RecyclerView recyclerView;
    private RequestQueue queue;
    private CharactersList charactersOnScreen;
    private boolean isAnimating = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        queue = Volley.newRequestQueue(this); // Nueva cola. Idealmente, en una app, se comparte una sola RequestQueue
        recyclerView = findViewById(R.id.recycler_view_characters); // Buscamos por ID el recyclerView recién añadido


        Intent intent = getIntent(); // Con esto accedemos al Intent que se usó para iniciar la actividad
        int clipId = intent.getIntExtra(VideoActivity.INTENT_CLIP_ID, -1);
        String clipUrl = intent.getStringExtra(VideoActivity.INTENT_CLIP_URL);

        Toast.makeText(this, "clipID: " + clipId, Toast.LENGTH_LONG).show();

        videoView = findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse(clipUrl));
        videoView.start();

        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // clipId fue inicializado más arriba, a traves los 'Extras'
                // .getCurrentPosition() devuelve el milisegundo actual del vídeo
                sendAppearancesRequest(clipId, videoView.getCurrentPosition());
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                activity.finish();
            }
        });

    }

    private void sendAppearancesRequest(int clipId, int milliseconds) {
        if (isAnimating) { return; }

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Server.name + "/appearances",
                null,

                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        CharactersList parsedServerResponse = new CharactersList(response);
                        setCharactersOnScreen(parsedServerResponse);
                    }
                },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(activity, "Error recibiendo los datos de personajes", Toast.LENGTH_SHORT).show();
                }
            });
    }

    private void setCharactersOnScreen(CharactersList charactersOnScreen) {
        this.charactersOnScreen = charactersOnScreen;
        CharactersAdapter myAdapter = new CharactersAdapter(this.charactersOnScreen);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAlpha(1);
        isAnimating = true;
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(recyclerView, "alpha", 1f, 0f);
        fadeOut.setDuration(500);
        fadeOut.setStartDelay(3000);
        fadeOut.start();

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isAnimating = false;
            }
        });
    }


}
