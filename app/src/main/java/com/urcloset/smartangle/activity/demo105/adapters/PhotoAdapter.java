package com.urcloset.smartangle.activity.demo105.adapters;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.urcloset.smartangle.R;
import com.urcloset.smartangle.activity.demo105.listeners.OnPhotoListener;
import com.urcloset.smartangle.activity.demo105.models.PhotoModel;


public class PhotoAdapter extends ListAdapter<PhotoModel, PhotoAdapter.PhotoHolder> {
    public OnPhotoListener photoListener;


    public PhotoAdapter() {
        super(diffCallback);
    }


    static DiffUtil.ItemCallback<PhotoModel> diffCallback = new DiffUtil.ItemCallback<PhotoModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull PhotoModel oldItem, @NonNull PhotoModel newItem) {
            return oldItem.photo.equals(newItem.photo);
        }

        @Override
        public boolean areContentsTheSame(@NonNull PhotoModel oldItem, @NonNull PhotoModel newItem) {
            return false;
        }
    };

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.photo_item,parent,false);
        return new PhotoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        if(getItem(position).photo==null)
        holder.ivPhoto.setImageResource(getItem(position).drawablePhoto);
        else         holder.ivPhoto.setImageURI(getItem(position).photo);


        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoListener.OnPhotoClick(position);
            }
        });


    }
   public void setOnPhotoListener(OnPhotoListener onPhotoListener){
        this.photoListener = onPhotoListener;

    }

    static class PhotoHolder extends RecyclerView.ViewHolder  {

        private ImageView ivPhoto;

        public PhotoHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);

        }

    }


    }

