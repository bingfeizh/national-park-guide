package com.hfad.nationalparksguide.Controller;

import com.hfad.nationalparksguide.data.model.Park;
import com.hfad.nationalparksguide.data.model.ParkList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ParkApiService {
    @GET("parks")
    Call<ParkList> getList(@Query("api_key") String apiKey, @Query("limit") int limit,  @Query("q") String q, @Query("fields") String image);

}
