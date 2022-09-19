package com.urcloset.smartangle.activity.demo105.models;

public class CategoryModel {
    public int id;
    public int photo;
    public boolean seelected = false;
   public CategoryModel(int id,int photo){
        this.id = id;
        this.photo = photo;
    }
    public CategoryModel(int id,int photo,boolean seelected){
        this.id = id;
        this.photo = photo;
        this.seelected = seelected;
    }
}
