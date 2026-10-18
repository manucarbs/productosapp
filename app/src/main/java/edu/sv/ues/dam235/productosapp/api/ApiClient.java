package edu.sv.ues.dam235.productosapp.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // IMPORTANTE: cambiar la IP por la de la computadora donde corre la API.
    // Celular real: IP local de la PC (hostname -I en Linux, ipconfig en Windows)
    // Emulador:     10.0.2.2
    private static final String BASE_URL =
            "http://192.168.1.5:8085/api/v1/demoapirestdam235/";

    private static ApiService apiService;

    public static ApiService getApiService() {
        if (apiService == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            apiService = retrofit.create(ApiService.class);
        }
        return apiService;
    }
}