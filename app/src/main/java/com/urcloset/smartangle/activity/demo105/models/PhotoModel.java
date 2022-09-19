package com.urcloset.smartangle.activity.demo105.models;

import android.net.Uri;

public class PhotoModel {
    public Uri photo;
    public int drawablePhoto;
    public PhotoModel(Uri photo){
        this.photo = photo;

    }
    public PhotoModel(int drawablePhoto){
        this.drawablePhoto = drawablePhoto;

    }


}
