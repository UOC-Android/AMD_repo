package com.andresgqjob.callingapiresttest;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataModel
{
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("supermarket")
    @Expose
    private String supermarket;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("reference_price")
    @Expose
    private Double refPrice;
    @SerializedName("reference_unit")
    @Expose
    private String refUnit;
    @SerializedName("insert_date")
    @Expose
    private String insertDate;
    @SerializedName("product_id")
    @Expose
    private String pid;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSupermarket() {
        return supermarket;
    }

    public void setSupermarket(String supermarket) {
        this.supermarket = supermarket;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Double getRefPrice() {
        return refPrice;
    }

    public void setRefPrice(Double refPrice) {
        this.refPrice = refPrice;
    }

    public String getRefUnit() {
        return refUnit;
    }

    public void setRefUnit(String refUnit) {
        this.refUnit = refUnit;
    }

    public String getInsertDate() {
        return insertDate;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

}
