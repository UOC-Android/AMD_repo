package com.dev.mrvazguen.indexproductorum.data.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Producto
{
    @SerializedName("url")
    @Expose
    private String url;
    @SerializedName("supermarket")
    @Expose
    private String supermercado;
    @SerializedName("category")
    @Expose
    private ArrayList<String>categoria;
    @SerializedName("name")
    @Expose
    private String nombre;
    @SerializedName("description")
    @Expose
    private String descripcion;
    @SerializedName("price")
    @Expose
    private double precio;
    @SerializedName("reference_price")
    @Expose
    private double refPvp;
    @SerializedName("reference_unit")
    @Expose
    private String refUd;
    @SerializedName("insert_date")
    @Expose
    private String insertarFecha;
    @SerializedName("product_id")
    @Expose
    private String pid;

    public Producto(){};
    public Producto(String nombre){
        this.nombre = nombre;
    }
    public Producto(String nombre, String descripcion, ArrayList<String> categoria, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.categoria = categoria;
        this.precio = precio;
    }

    public Producto(String url, String supermarket, ArrayList<String> categoria, String name, String description, Double price, Double refPrice, String refUnit, String insertDate, String pid) {
        this.url = url;
        this.supermercado = supermarket;
        this.categoria = categoria;
        this.nombre = name;
        this.descripcion = description;
        this.precio = price;
        this.refPvp = refPrice;
        this.refUd = refUnit;
        this.insertarFecha = insertDate;
        this.pid = pid;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getSupermercado() {
        return supermercado;
    }

    public void setSupermercado(String supermercado) {
        this.supermercado = supermercado;
    }

    public ArrayList<String> getCategoria() {
        return categoria;
    }

    public void setCategoria(ArrayList<String> categoria) {
        this.categoria = categoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getRefPvp() {
        return refPvp;
    }

    public void setRefPvp(Double refPvp) {
        this.refPvp = refPvp;
    }

    public String getRefUd() {
        return refUd;
    }

    public void setRefUd(String refUd) {
        this.refUd = refUd;
    }

    public String getInsertarFecha() {
        return insertarFecha;
    }

    public void setInsertarFecha(String insertarFecha) {
        this.insertarFecha = insertarFecha;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @NonNull
    @Override
    public String toString() {
        return "Producto{" +
                "url='" + url + '\'' +
                ", supermercado='" + supermercado + '\'' +
                ", categoria=" + categoria +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", refPvp=" + refPvp +
                ", refUd='" + refUd + '\'' +
                ", insertarFecha='" + insertarFecha + '\'' +
                ", pid='" + pid + '\'' +
                '}';
    }
}
