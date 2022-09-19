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
import com.urcloset.smartangle.activity.demo105.listeners.OnCategoryListener;
import com.urcloset.smartangle.activity.demo105.models.CategoryModel;


public class CatAdapter extends ListAdapter<CategoryModel, CatAdapter.CatHolder> {
    public OnCategoryListener onCategoryListener;



    public CatAdapter() {
        super(diffCallback);
    }


    static DiffUtil.ItemCallback<CategoryModel> diffCallback = new DiffUtil.ItemCallback<CategoryModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull CategoryModel oldItem, @NonNull CategoryModel newItem) {
            return oldItem.id==newItem.id &&oldItem.seelected == newItem.seelected;
        }

        @Override
        public boolean areContentsTheSame(@NonNull CategoryModel oldItem, @NonNull CategoryModel newItem) {
            return oldItem.seelected == newItem.seelected;
        }
    };

    @NonNull
    @Override
    public CatHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item,parent,false);
        return new CatHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CatHolder holder, int position) {
        holder.ivPhoto.setImageResource(getItem(position).photo);
        if(getItem(position).seelected){
            holder.rv_cat.setBackground(holder.itemView.getResources().getDrawable(R.drawable.category_selected_item_bg));

        }
        else{
            holder.rv_cat.setBackground(holder.itemView.getResources().getDrawable(R.drawable.category_unselected_item_bg));

        }


        holder.ivPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCategoryListener.onCategoryClick(position);
            }
        });


    }

    public void setOnCategoryListener(OnCategoryListener onCategoryListener) {
        this.onCategoryListener = onCategoryListener;
    }

    static class CatHolder extends RecyclerView.ViewHolder  {

        private ImageView ivPhoto;
        private RelativeLayout rv_cat;

        public CatHolder(@NonNull View itemView) {
            super(itemView);
            ivPhoto = itemView.findViewById(R.id.iv_photo);
            rv_cat = itemView.findViewById(R.id.rv_cat);

        }

    }


}

