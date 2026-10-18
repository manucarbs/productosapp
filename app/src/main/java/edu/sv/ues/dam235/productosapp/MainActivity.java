package edu.sv.ues.dam235.productosapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import edu.sv.ues.dam235.productosapp.api.ApiClient;
import edu.sv.ues.dam235.productosapp.models.LoginRequest;
import edu.sv.ues.dam235.productosapp.models.TokenResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private EditText etUser, etPass;
    private Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etUser = findViewById(R.id.etUser);
        etPass = findViewById(R.id.etPass);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener()  {

            @Override
            public void onClick(View v) {
                hacerLogin();
            }
        });

        findViewById(R.id.btnIrRegistro).setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, RegisterActivity.class)));
    }

    private void hacerLogin() {
        String user = etUser.getText().toString().trim();
        String pass = etPass.getText().toString();

        if (user.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Ingrese correo y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        btnLogin.setEnabled(false);

        ApiClient.getApiService().login(new LoginRequest(user, pass))
                .enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                        btnLogin.setEnabled(true);
                        if (response.isSuccessful() && response.body() != null) {
                            Intent intent = new Intent(MainActivity.this, ProductsActivity.class);
                            intent.putExtra("token", response.body().getToken());
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "Credenciales incorrectas", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                        btnLogin.setEnabled(true);
                        Toast.makeText(MainActivity.this,
                                "No se pudo conectar: " + t.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }
}