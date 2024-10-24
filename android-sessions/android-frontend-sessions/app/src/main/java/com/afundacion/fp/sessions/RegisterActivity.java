package com.afundacion.fp.sessions;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    // Creamos dos atributos nuevos
    private EditText editTextUser;
    private EditText editTextPassword;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Los inicializamos a partir de su contraparte en el XML
        editTextUser = findViewById(R.id.editTextUser);
        editTextPassword = findViewById(R.id.editTextPassword);
        registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Código que se ejecuta al hacer clic en el botón
                Toast.makeText(getApplicationContext(), "Usuario: " + editTextUser.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
