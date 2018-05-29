package com.example.apartmentprices.rest;

import com.google.gson.annotations.SerializedName;

public class MaterialModel {
    @SerializedName("name_mtrl")
    private String nameMtrl;

    public String getNameMtrl() { return nameMtrl; }
}
