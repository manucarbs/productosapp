package edu.sv.ues.dam235.productosapp;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.sv.ues.dam235.productosapp.api.ApiClient;
import edu.sv.ues.dam235.productosapp.models.Product;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductsActivity extends AppCompatActivity {

    private TextView tvEstado;
    private RecyclerView rvProductos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        tvEstado = findViewById(R.id.tvEstado);
        rvProductos = findViewById(R.id.rvProductos);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));

        String token = getIntent().getStringExtra("token");
        cargarProductos(token);
    }

    private void cargarProductos(String token) {
        ApiClient.getApiService().getProducts("Bearer " + token)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            tvEstado.setText(response.body().size() + " productos");
                            rvProductos.setAdapter(new ProductAdapter(response.body()));
                        } else if (response.code() == 204) {
                            tvEstado.setText("No hay productos registrados");
                        } else {
                            tvEstado.setText("Error " + response.code() + ": sesión no válida");
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Product>> call, Throwable t) {
                        tvEstado.setText("No se pudo conectar: " + t.getMessage());
                    }
                });
    }
}