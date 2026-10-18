package edu.sv.ues.dam235.productosapp.models;

public class ProductRequest {
    private String name;
    private boolean status;

    public ProductRequest(String name, boolean status) {
        this.name = name;
        this.status = status;
    }

    public String getName() { return name; }
    public boolean isStatus() { return status; }
}