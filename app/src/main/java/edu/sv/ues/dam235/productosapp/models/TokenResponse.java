package edu.sv.ues.dam235.productosapp.models;

public class TokenResponse {
    private String token;
    private String expireIn;
    private String msj;

    public String getToken() { return token; }
    public String getExpireIn() { return expireIn; }
    public String getMsj() { return msj; }
}