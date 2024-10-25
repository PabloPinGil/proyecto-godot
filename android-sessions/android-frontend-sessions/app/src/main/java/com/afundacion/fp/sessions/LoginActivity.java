package com.afundacion.fp.sessions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private Button buttonQuieresCuenta;
    private Button buttonInicioSesion;
    private RequestQueue queue;
    private Context context;
    private EditText editTextPasswordLogin;
    private EditText editTextUserLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        queue = Volley.newRequestQueue(this);
        buttonQuieresCuenta = findViewById(R.id.buttonQuieresCuenta);
        buttonInicioSesion = findViewById(R.id.buttonInicioSesion);
        editTextPasswordLogin = findViewById(R.id.editTextPasswordLogin);
        editTextUserLogin = findViewById(R.id.editTextUserLogin);
        queue = Volley.newRequestQueue(this);
        context = this;

        buttonQuieresCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonInicioSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendPostRequest();
            }
        });
    }

    private void sendPostRequest() {
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", editTextUserLogin.getText().toString() /* .getText().toString() sobre tu instancia de editText para el nombre de usuario */ );
            requestBody.put("password", editTextPasswordLogin.getText().toString() /* .getText().toString() sobre tu instancia de editText para la contraseña*/ );
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                Server.name + "/sessions",
                requestBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String receivedToken;
                        try {
                            receivedToken = response.getString("sessionToken");
                        } catch (JSONException e) {
                            // Si el JSON de la respuesta NO contiene "sessionToken", vamos a lanzar
                            // una RuntimeException para que la aplicación rompa.
                            // En preferible que sea NOTORIO el problema del servidor, pues desde
                            // la aplicación no podemos hacer nada. Estamos 'vendidos'.
                            throw new RuntimeException(e);
                        }
                        // Si la respuesta está OK, mostramos un Toast
                        Toast.makeText(context, "Token de sesión: " + receivedToken, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null) {
                            Toast.makeText(LoginActivity.this, "La conexión no se ha establecido", Toast.LENGTH_LONG).show();
                        } else {
                            int serverCode = error.networkResponse.statusCode;
                            Toast.makeText(LoginActivity.this, "El servidor respondió con: " + serverCode, Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );

        this.queue.add(request);
    }
}
