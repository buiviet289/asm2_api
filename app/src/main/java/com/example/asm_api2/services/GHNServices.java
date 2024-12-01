package com.example.asm_api2.services;

import com.example.asm_api2.model.District;
import com.example.asm_api2.model.DistrictRequest;
import com.example.asm_api2.model.GHNOrderRequest;
import com.example.asm_api2.model.GHNOrderRespone;
import com.example.asm_api2.model.Province;
import com.example.asm_api2.model.ResponeGHN;
import com.example.asm_api2.model.Ward;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GHNServices {
    public static String GHN_URL = "https://dev-online-gateway.ghn.vn/";
    @POST("shiip/public-api/v2/shipping-order/create")
    Call<ResponeGHN<GHNOrderRespone>> GHNOrder (@Body GHNOrderRequest ghnOrderRequest);
    @GET("/shiip/public-api/master-data/province")
    Call<ResponeGHN<ArrayList<Province>>> getListProvince();

    @POST("/shiip/public-api/master-data/district")
    Call<ResponeGHN<ArrayList<District>>> getListDistrict(@Body DistrictRequest districtRequest);

    @GET("/shiip/public-api/master-data/ward")
    Call<ResponeGHN<ArrayList<Ward>>> getListWard(@Query("district_id") int district_id);
}

