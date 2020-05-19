package org.tensorflow.lite.examples.detection;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecycleItApi {
    String BASE_URL = "https://recycleitbackend.herokuapp.com/";
    String FEED = "app";

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @GET("app/add_alert")
    Call<ResponseBody> addAlert(@Query("recemail") String email,
                                @Query("latitude") double latitude,
                                @Query("longitude") double longitude,
                                @Query("address") String address,
                                @Query("country") String country,
                                @Query("city") String city,
                                @Query("type") String type,
                                @Query("item_list") String itemList);
}
