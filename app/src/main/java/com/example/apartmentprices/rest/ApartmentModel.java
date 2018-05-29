package com.example.apartmentprices.rest;

import com.google.gson.annotations.SerializedName;

public class ApartmentModel {
    @SerializedName("room_no")
    private Integer room;
    @SerializedName("area")
    private Integer area;
    @SerializedName("h_dstr")
    private Integer dstr;
    @SerializedName("h_mtrl")
    private Integer mtrl;
    @SerializedName("balcony")
    private Boolean balcony;
    @SerializedName("total_floors")
    private Integer floors;
    @SerializedName("first_floor")
    private Boolean first_floor;
    @SerializedName("last_floor")
    private Boolean last_floor;
    @SerializedName("price")
    private Float price;

    public Integer getRoom() { return room; }
    public Integer getArea() { return area; }
    public Integer getDstr() { return dstr; }
    public Integer getMtrl() { return mtrl; }
    public Boolean getBalcony() { return balcony; }
    public Integer getFloors() { return floors; }
    public Boolean getFirstFloor() { return first_floor; }
    public Boolean getLastFloor() { return last_floor; }
    public Float getPrice() { return price; }

    @Override
    public String toString() {
        return "Apartment {\n" +
                "   room_no: " + room + "\n" +
                "   area: " + area + "\n" +
                "   h_dstr: " + dstr + "\n" +
                "   h_mtrl: " + mtrl + "\n" +
                "   balcony: " + balcony + "\n" +
                "   total_floors: " + floors + "\n" +
                "   first_floor: " + first_floor + "\n" +
                "   last_floor: " + last_floor + "\n" +
                "   price: " + price + "\n}";
    }
}
