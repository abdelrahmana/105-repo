package com.urcloset.smartangle.activity.demo105.adapters;

import android.content.Context;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.urcloset.smartangle.R;
import com.urcloset.smartangle.activity.demo105.listeners.OnColorListener;
import com.urcloset.smartangle.activity.demo105.models.ColorModel;


public class ColorsAdapter extends ListAdapter<ColorModel, ColorsAdapter.ColorModelViewHolder> {


    public ColorsAdapter() {
        super(diffCallback);
    }


    static DiffUtil.ItemCallback<ColorModel> diffCallback = new DiffUtil.ItemCallback<ColorModel>() {
        @Override
        public boolean areItemsTheSame(@NonNull ColorModel oldItem, @NonNull ColorModel newItem) {
            return oldItem.color.equals(newItem.color);
        }

        @Override
        public boolean areContentsTheSame(@NonNull ColorModel oldItem, @NonNull ColorModel newItem) {
            return false;
        }
    };

    @NonNull
    @Override
    public ColorModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.color_item,parent,false);
        return new ColorModelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ColorModelViewHolder holder, int position) {
        holder.colorImage.getBackground().setColorFilter(Color.parseColor(getItem(position).color), PorterDuff.Mode.MULTIPLY );
        if(getItem(position).selected){
            holder.ivColorSelected.setVisibility(View.VISIBLE);

        }
        else{
            holder.ivColorSelected.setVisibility(View.INVISIBLE);

        }
        holder.setOnColorListener(new OnColorListener() {
            @Override
            public void onColorClick(int colorModel) {
               ColorModel color = getCurrentList().get(colorModel);

               if(color.selected) {
                   color.selected = false;
                   holder.ivColorSelected.setVisibility(View.INVISIBLE);
               }
               else {
                   color.selected = true;
                   holder.ivColorSelected.setVisibility(View.VISIBLE);

               }
            }
        });

    }

    static class ColorModelViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private RelativeLayout colorImage;
        private OnColorListener onColorListener;
        private ImageView ivColorSelected;

        public ColorModelViewHolder(@NonNull View itemView) {
            super(itemView);
            colorImage = itemView.findViewById(R.id.rv_color);
            ivColorSelected = itemView.findViewById(R.id.iv_color_selected);
            itemView.setOnClickListener(this);
        }
        void setOnColorListener(OnColorListener onColorListener){
            this.onColorListener = onColorListener;

        }


        @Override
        public void onClick(View v) {
            onColorListener.onColorClick(getAdapterPosition());

        }
    }
}
