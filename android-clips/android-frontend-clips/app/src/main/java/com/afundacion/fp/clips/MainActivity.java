package com.afundacion.fp.clips;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private RequestQueue queue;
    private Context context = this;
    private ConstraintLayout mainLayout;
    private ProgressBar progressBar;
    private ClipsList clips;
    private RecyclerView recyclerView;

    public void setClips(ClipsList clips) {
        this.clips = clips;
        ClipsAdapter myAdapter = new ClipsAdapter(this.clips);
        recyclerView.setAdapter(myAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public ClipsList getClipsForTest() {
        return clips;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.recyclerView = findViewById(R.id.recyclerView);
        this.queue = Volley.newRequestQueue(context);
        this.mainLayout = findViewById(R.id.main_layout);
        this.progressBar = findViewById(R.id.barraProgreso);

        requestClipList();

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                Server.name + "/health",
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Toast.makeText(MainActivity.this, "Response ok: " + response.getString("status"), Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(context, "Could not establish connection", Toast.LENGTH_SHORT).show();

                        } else {
                            int serverCode = error.networkResponse.statusCode;
                            Toast.makeText(context, "Server status: " + serverCode, Toast.LENGTH_SHORT).show();
                        }
                }
        }
        );
        this.queue.add(request);
    }

    private void requestClipList() {
        progressBar.setVisibility(View.VISIBLE);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                Server.name + "/clips",
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressBar.setVisibility(View.INVISIBLE);
                        Snackbar.make(mainLayout, "List received", Snackbar.LENGTH_LONG).show();

                        setClips(new ClipsList(response));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressBar.setVisibility(View.INVISIBLE);
                        if (error.networkResponse == null) {
                           Snackbar.make(mainLayout, "Could not establish connection", Snackbar.LENGTH_LONG).show();

                        } else {
                            int serverCode = error.networkResponse.statusCode;
                            Snackbar.make(mainLayout, "Server responded with " + serverCode, Snackbar.LENGTH_LONG).show();
                        }
                    }
                }
        );
        this.queue.add(request);
    }
}

