package com.example.apartmentprices.rest;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://rjena.pythonanywhere.com/";

    public static RjenaInterface getAPIService() {
        return RetrofitClient.getClient(BASE_URL).create(RjenaInterface.class);
    }
}
