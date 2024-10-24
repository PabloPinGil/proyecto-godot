package com.afundacion.fp.sessions;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    // Creamos dos atributos nuevos
    private EditText editTextUser;
    private EditText editTextPassword;
    private Button registerButton;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Los inicializamos a partir de su contraparte en el XML
        editTextUser = findViewById(R.id.editTextUser);
        editTextPassword = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.registerButton);

        requestQueue = Volley.newRequestQueue(this);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Código que se ejecuta al hacer clic en el botón
                Toast.makeText(getApplicationContext(), "Usuario: " + editTextUser.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendPostRequest() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", editTextUser.getText().toString());
            requestBody.put("password", editTextPassword.getText().toString());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Server.name + "/users",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(RegisterActivity.this, "Usuario registrado", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(RegisterActivity.this, "La conexión no se ha establecido", Toast.LENGTH_LONG).show();
                        } else {
                            // El servidor ha dado una respuesta de error

                            // La siguiente variable contendrá el código HTTP,
                            // por ejemplo 401, 500,...
                            int serverCode = error.networkResponse.statusCode;
                            Toast.makeText(RegisterActivity.this, "El servidor respondió con: " + serverCode, Toast.LENGTH_LONG).show();
                        }

                    }
                }
        );

        this.requestQueue.add(request);
    }


}
