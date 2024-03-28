package com.app_nccaa.nccaa.Model;

import java.util.ArrayList;

public class CasesModel {

    private String id;
    private String title;
    private String date;
    private String start_time;
    private String end_time;
    private String duration;
    private String age;
    private String asa;

    private ArrayList<CategoriesModel> categoriesArrayList;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<CategoriesModel> getCategoriesArrayList() {
        return categoriesArrayList;
    }

    public void setCategoriesArrayList(ArrayList<CategoriesModel> categoriesArrayList) {
        this.categoriesArrayList = categoriesArrayList;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAsa() {
        return asa;
    }

    public void setAsa(String asa) {
        this.asa = asa;
    }
}
