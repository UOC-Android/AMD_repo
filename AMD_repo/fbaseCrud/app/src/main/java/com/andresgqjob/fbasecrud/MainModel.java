package com.andresgqjob.fbasecrud;

import androidx.annotation.NonNull;

public class MainModel
{
    String name;
    String course;
    String email;
    String url;

    public MainModel() {}

    public MainModel(String name, String course, String email, String url) {
        this.name = name;
        this.course = course;
        this.email = email;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @NonNull
    @Override
    public String toString() {
        return "MainModel{" +
                "name='" + name + '\'' +
                ", course='" + course + '\'' +
                ", email='" + email + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
