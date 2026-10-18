package edu.sv.ues.dam235.productosapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.sv.ues.dam235.productosapp.api.ApiClient;
import edu.sv.ues.dam235.productosapp.models.RegisterRequest;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etNombre, etApellido, etCorreo, etClave;
    private Button btnRegistrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etNombre = findViewById(R.id.etNombre);
        etApellido = findViewById(R.id.etApellido);
        etCorreo = findViewById(R.id.etCorreo);
        etClave = findViewById(R.id.etClave);
        btnRegistrar = findViewById(R.id.btnRegistrar);

        btnRegistrar.setOnClickListener(v -> registrar());
        findViewById(R.id.btnVolver).setOnClickListener(v -> finish());
    }

    private void registrar() {
        String nombre = etNombre.getText().toString().trim();
        String apellido = etApellido.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String clave = etClave.getText().toString();

        if (nombre.isEmpty() || apellido.isEmpty() || correo.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        btnRegistrar.setEnabled(false);

        ApiClient.getApiService()
                .register(new RegisterRequest(nombre, apellido, correo, clave))
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        btnRegistrar.setEnabled(true);
                        if (response.code() == 201) {
                            Toast.makeText(RegisterActivity.this,
                                    "Cuenta creada. Ya puede iniciar sesión",
                                    Toast.LENGTH_LONG).show();
                            finish();
                        } else if (response.code() == 409) {
                            Toast.makeText(RegisterActivity.this,
                                    "Ese correo ya está registrado",
                                    Toast.LENGTH_LONG).show();
                        } else if (response.code() == 400) {
                            Toast.makeText(RegisterActivity.this,
                                    "Datos incompletos o no válidos",
                                    Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(RegisterActivity.this,
                                    "No se pudo crear la cuenta (error " + response.code() + ")",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        btnRegistrar.setEnabled(true);
                        Toast.makeText(RegisterActivity.this,
                                "No se pudo conectar: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}