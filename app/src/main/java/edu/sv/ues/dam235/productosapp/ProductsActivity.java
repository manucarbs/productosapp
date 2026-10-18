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

import android.content.Intent;
import android.widget.Button;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import edu.sv.ues.dam235.productosapp.models.Product;
import edu.sv.ues.dam235.productosapp.models.ProductRequest;

public class ProductsActivity extends AppCompatActivity {

    private TextView tvEstado;
    private RecyclerView rvProductos;

    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        tvEstado = findViewById(R.id.tvEstado);
        rvProductos = findViewById(R.id.rvProductos);
        rvProductos.setLayoutManager(new LinearLayoutManager(this));

        token = getIntent().getStringExtra("token");
        cargarProductos(token);

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> cerrarSesion());

        Button btnAgregar = findViewById(R.id.btnAgregar);
        btnAgregar.setOnClickListener(v -> mostrarDialogoAgregar());
    }

    private void cargarProductos(String token) {
        ApiClient.getApiService().getProducts("Bearer " + token)
                .enqueue(new Callback<List<Product>>() {
                    @Override
                    public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            tvEstado.setText(response.body().size() + " productos");
                            rvProductos.setAdapter(new ProductAdapter(response.body(), ProductsActivity.this::mostrarDialogoEditar));
                        } else if (response.code() == 204) {
                            tvEstado.setText("No hay productos registrados");
                            rvProductos.setAdapter(null);
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

    private void cerrarSesion() {
        ApiClient.getApiService().logout("Bearer " + token)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        volverAlLogin();
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        // Aunque falle la red, se sale igual de la app
                        volverAlLogin();
                    }
                });
    }

    private void volverAlLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void mostrarDialogoAgregar() {
        View vista = LayoutInflater.from(this).inflate(R.layout.dialog_product, null);
        EditText etNombre = vista.findViewById(R.id.etNombre);
        CheckBox cbActivo = vista.findViewById(R.id.cbActivo);

        new AlertDialog.Builder(this)
                .setTitle("Nuevo producto")
                .setView(vista)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    if (nombre.isEmpty()) {
                        Toast.makeText(this, "Escriba un nombre", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    crearProducto(nombre, cbActivo.isChecked());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void crearProducto(String nombre, boolean activo) {
        ApiClient.getApiService()
                .createProduct("Bearer " + token, new ProductRequest(nombre, activo))
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ProductsActivity.this,
                                    "Producto agregado", Toast.LENGTH_SHORT).show();
                            cargarProductos(token);
                        } else {
                            Toast.makeText(ProductsActivity.this,
                                    "No se pudo guardar (error " + response.code() + ")",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Toast.makeText(ProductsActivity.this,
                                "No se pudo conectar: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
    private void mostrarDialogoEditar(Product producto) {
        View vista = LayoutInflater.from(this).inflate(R.layout.dialog_product, null);
        EditText etNombre = vista.findViewById(R.id.etNombre);
        CheckBox cbActivo = vista.findViewById(R.id.cbActivo);
        etNombre.setText(producto.getName());
        cbActivo.setChecked(producto.isStatus());

        new AlertDialog.Builder(this)
                .setTitle("Editar producto")
                .setView(vista)
                .setPositiveButton("Guardar", (dialog, which) -> {
                    String nombre = etNombre.getText().toString().trim();
                    if (nombre.isEmpty()) {
                        Toast.makeText(this, "Escriba un nombre", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    actualizarProducto(producto.getCode(), nombre, cbActivo.isChecked());
                })
                .setNeutralButton("Eliminar", (dialog, which) -> confirmarEliminar(producto))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void actualizarProducto(int code, String nombre, boolean activo) {
        ApiClient.getApiService()
                .updateProduct("Bearer " + token, code, new ProductRequest(nombre, activo))
                .enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ProductsActivity.this,
                                    "Producto actualizado", Toast.LENGTH_SHORT).show();
                            cargarProductos(token);
                        } else {
                            Toast.makeText(ProductsActivity.this,
                                    "No se pudo actualizar (error " + response.code() + ")",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {
                        Toast.makeText(ProductsActivity.this,
                                "No se pudo conectar: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void confirmarEliminar(Product producto) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar producto")
                .setMessage("¿Eliminar \"" + producto.getName() + "\"?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarProducto(producto.getCode()))
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarProducto(int code) {
        ApiClient.getApiService()
                .deleteProduct("Bearer " + token, code)
                .enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(ProductsActivity.this,
                                    "Producto eliminado", Toast.LENGTH_SHORT).show();
                            cargarProductos(token);
                        } else {
                            Toast.makeText(ProductsActivity.this,
                                    "No se pudo eliminar (error " + response.code() + ")",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(ProductsActivity.this,
                                "No se pudo conectar: " + t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
    }
}