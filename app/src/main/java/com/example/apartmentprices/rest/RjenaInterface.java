package com.example.apartmentprices.rest;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RjenaInterface {
    @FormUrlEncoded
    @POST("api/")
    Call<ApartmentModel> postData(
            @Field("room_no") int room,
            @Field("area") int area,
            @Field("h_dstr") int dstr,
            @Field("h_mtrl") int mtrl,
            @Field("balcony") boolean balcony,
            @Field("total_floors") int floors,
            @Field("first_floor") boolean first,
            @Field("last_floor") boolean last
    );

    @GET("api/")
    Call<ApartmentModel[]> getApartments(@Query("format") String format);

    @GET("districts/")
    Call<DistrictModel[]> getDistricts(@Query("format") String format);

    @GET("materials/")
    Call<MaterialModel[]> getMaterials(@Query("format") String format);
}
