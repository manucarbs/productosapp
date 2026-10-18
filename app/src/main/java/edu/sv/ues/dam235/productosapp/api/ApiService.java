package edu.sv.ues.dam235.productosapp.api;

import java.util.List;

import edu.sv.ues.dam235.productosapp.models.LoginRequest;
import edu.sv.ues.dam235.productosapp.models.Product;
import edu.sv.ues.dam235.productosapp.models.TokenResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("auth/login")
    Call<TokenResponse> login(@Body LoginRequest request);

    @GET("products")
    Call<List<Product>> getProducts(@Header("Authorization") String authorization);
}