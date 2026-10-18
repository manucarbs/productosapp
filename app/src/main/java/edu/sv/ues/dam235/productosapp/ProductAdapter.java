package edu.sv.ues.dam235.productosapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.sv.ues.dam235.productosapp.models.Product;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    public interface OnProductClickListener {
        void onProductClick(Product producto);
    }

    private final List<Product> productos;
    private final OnProductClickListener listener;

    public ProductAdapter(List<Product> productos, OnProductClickListener listener) {
        this.productos = productos;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(vista);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product producto = productos.get(position);
        holder.tvNombre.setText(producto.getName());

        String estado = producto.isStatus() ? "Activo" : "Inactivo";
        holder.tvDetalle.setText("Código: " + producto.getCode() + " · " + estado);
        holder.itemView.setOnClickListener(v -> listener.onProductClick(producto));
    }

    @Override
    public int getItemCount() {
        return productos.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvDetalle;

        ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvDetalle = itemView.findViewById(R.id.tvDetalle);
        }
    }
}