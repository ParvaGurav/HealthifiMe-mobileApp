package com.example.myapplication;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("/userDetails/loginUser/{user_name}/{password}")
    Call<UserResponse> loginUser(
            @Path("user_name") String userName,
            @Path("password") String password
    );

    @Headers("Content-Type: application/json")
    @POST("userDetails/saveUser")  // Update the endpoint accordingly
    Call<UserResponse> registerUser(@Body UserResponse request);

    @POST("/userDetails/getUserDetails/{user_id}")
    Call<UserResponse> userInfo(@Path("user_id") String user_id);


    @POST("/userDetails/updateWeight/{weight}/{user_id}")
    Call<UserResponse> userWeight(@Path("weight") int weight,
                                  @Path("user_id") String user_id);
}
