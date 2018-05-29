package com.example.apartmentprices.rest;

import com.google.gson.annotations.SerializedName;

public class DistrictModel {
    @SerializedName("name_dstr")
    private String nameDstr;

    public String getNameDstr() { return nameDstr; }
}
