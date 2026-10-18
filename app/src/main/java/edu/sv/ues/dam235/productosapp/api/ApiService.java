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
import edu.sv.ues.dam235.productosapp.models.ProductRequest;
import retrofit2.http.DELETE;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    @POST("auth/login")
    Call<TokenResponse> login(@Body LoginRequest request);

    @POST("auth/logout")
    Call<Void> logout(@Header("Authorization") String authorization);

    @GET("products")
    Call<List<Product>> getProducts(@Header("Authorization") String authorization);

    @POST("products")
    Call<Product> createProduct(@Header("Authorization") String authorization,
                                @Body ProductRequest request);

    @PUT("products/{code}")
    Call<Product> updateProduct(@Header("Authorization") String authorization,
                                @Path("code") int code,
                                @Body ProductRequest request);

    @DELETE("products/{code}")
    Call<Void> deleteProduct(@Header("Authorization") String authorization,
                             @Path("code") int code);
}