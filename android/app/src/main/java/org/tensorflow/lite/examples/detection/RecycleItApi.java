package org.tensorflow.lite.examples.detection;

import java.util.ArrayList;

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

    @GET("app/add_alert")
    Call<ResponseBody> addAlertWithBook(@Query("recemail") String email,
                                @Query("latitude") double latitude,
                                @Query("longitude") double longitude,
                                @Query("address") String address,
                                @Query("country") String country,
                                @Query("city") String city,
                                @Query("type") String type,
                                @Query("item_list") String itemList,
                                        @Query("book") String book);

    @GET("app/login_recycler")
    Call<ResponseBody> login(@Query("email") String email, @Query("password") String password);

    @GET("app/verify_r_username")
    Call<ResponseBody> username(@Query("username") String username);

    @GET("app/verify_r_email")
    Call<ResponseBody> email(@Query("email") String email);

    @GET("app/verify_r_phone")
    Call<ResponseBody> phone(@Query("phone") String phone);

    @GET("app/add_user")
    Call<ResponseBody> createAccount(@Query("email") String email,
                             @Query("username") String username,
                             @Query("password") String password,
                             @Query("address") String address,
                             @Query("country") String country,
                             @Query("city") String city,
                             @Query("phone") String phone);

    @GET("app/update_rec")
    Call<ResponseBody> updateAccount(@Query("email1") String email1,
                                     @Query("email2") String email2,
                                     @Query("username") String username,
                                     @Query("password") String password,
                                     @Query("address") String address,
                                     @Query("country") String country,
                                     @Query("city") String city,
                                     @Query("phone") String phone);

    @GET("app/add_disposal_info")
    Call<ResponseBody> addPersonalDisposal(@Query("email") String email,
                                     @Query("item_list") String itemList,
                                     @Query("type") String type);

    @GET("app/get_disposal_info")
    Call<ArrayList<Alert>> getPersonalDisposal(@Query("email") String email);

    @GET("app/get_rec_alert_conf")
    Call<ArrayList<AlertConfirmation>> getAlertConfirmations(@Query("email") String email);

    @GET("app/update_alert_conf")
    Call<ResponseBody> response(@Query("id") int id,
                                    @Query("response") String response);


}
