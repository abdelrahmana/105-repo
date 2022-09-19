package com.urcloset.smartangle.activity.demo105;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.urcloset.smartangle.R;
import com.urcloset.smartangle.activity.demo105.adapters.CatAdapter;
import com.urcloset.smartangle.activity.demo105.adapters.ColorsAdapter;
import com.urcloset.smartangle.activity.demo105.adapters.PhotoAdapter;
import com.urcloset.smartangle.activity.demo105.listeners.OnCategoryListener;
import com.urcloset.smartangle.activity.demo105.listeners.OnPhotoListener;
import com.urcloset.smartangle.activity.demo105.models.CategoryModel;
import com.urcloset.smartangle.activity.demo105.models.ColorModel;
import com.urcloset.smartangle.activity.demo105.models.PhotoModel;

import java.util.ArrayList;
import java.util.List;

public class AddProductActivity extends AppCompatActivity {
    RecyclerView productColors, productsImages,categories;
    List<ColorModel> colors = new ArrayList();
    ColorsAdapter colorsAdapter = new ColorsAdapter();
    TextView textNegotiableYes, textNegotiableNo, textAny,
            textUsed, textNew,tv_note,tvBaY,tvBaN,tvIaY,tvIaN;
    List<PhotoModel> photoModelList = new ArrayList<>();
    PhotoAdapter photoAdapter = new PhotoAdapter();
    List<CategoryModel> categoryModelList = new ArrayList<>();
    CatAdapter catAdapter = new CatAdapter();
    Spinner duration;
    EditText name,desc;

    final static int RESULT_LOAD_IMAGE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.long_act);
        init_views();
        init_actions();
        load_data();









    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            photoModelList.remove(photoModelList.size()-1);
            photoModelList.add(new PhotoModel(selectedImage));
            photoModelList.add(new PhotoModel(R.drawable.dr_add_photo));
            photoAdapter.submitList(photoModelList);
            photoAdapter.notifyDataSetChanged();
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    void init_views(){
        productsImages = findViewById(R.id.rv_photos);
        textNegotiableNo = findViewById(R.id.tv_no);
        textNegotiableYes = findViewById(R.id.tv_yes);
        tvBaY = findViewById(R.id.tv_ba_y);
        tvBaN = findViewById(R.id.tv_ba_n);
        name = findViewById(R.id.name);
        textAny = findViewById(R.id.tv_any);
        textNew = findViewById(R.id.tv_new);
        textUsed = findViewById(R.id.tv_used);
        categories = findViewById(R.id.rv_categories);
        duration = findViewById(R.id.sp_duration);
        tv_note  = findViewById(R.id.tv_note);
        tvIaY = findViewById(R.id.tv_ia_y);
        tvIaN = findViewById(R.id.tv_ia_n);
        desc = findViewById(R.id.desc);
        productColors = findViewById(R.id.rv_colors);


    }
    void init_actions(){
        catAdapter.setOnCategoryListener(new OnCategoryListener() {
            @Override
            public void onCategoryClick(int position) {
                for(int i=0;i<=categoryModelList.size()-1;i++){
                    if(i!=position)
                        categoryModelList.get(i).seelected = false;

                    categoryModelList.get(position).seelected = true;
                    catAdapter.submitList(categoryModelList);
                    catAdapter.notifyDataSetChanged();


                }

            }
        });
        textNegotiableNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textNegotiableNo.setTextColor(Color.parseColor("#ffffff"));
                textNegotiableNo.setBackground(getResources().getDrawable(R.drawable.yes_background));
                textNegotiableYes.setTextColor(Color.parseColor("#000000"));
                textNegotiableYes.setBackground(getResources().getDrawable(R.drawable.ng_background));


            }
        });
        photoAdapter.setOnPhotoListener(new OnPhotoListener() {
            @Override
            public void OnPhotoClick(int photoModel) {
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);


            }
        });
        textNegotiableYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textNegotiableYes.setTextColor(Color.parseColor("#ffffff"));
                textNegotiableYes.setBackground(getResources().getDrawable(R.drawable.yes_background));
                textNegotiableNo.setTextColor(Color.parseColor("#000000"));
                textNegotiableNo.setBackground(getResources().getDrawable(R.drawable.ng_background));

            }
        });
        textAny.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textAny.setTextColor(Color.parseColor("#ffffff"));
                textAny.setBackground(getResources().getDrawable(R.drawable.yes_background));
                textNew.setTextColor(Color.parseColor("#000000"));
                textNew.setBackground(getResources().getDrawable(R.drawable.ng_background));
                textUsed.setTextColor(Color.parseColor("#000000"));
                textUsed.setBackground(getResources().getDrawable(R.drawable.ng_background));


            }
        });
        textUsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textUsed.setTextColor(Color.parseColor("#ffffff"));
                textUsed.setBackground(getResources().getDrawable(R.drawable.yes_background));
                textNew.setTextColor(Color.parseColor("#000000"));
                textNew.setBackground(getResources().getDrawable(R.drawable.ng_background));
                textAny.setTextColor(Color.parseColor("#000000"));
                textAny.setBackground(getResources().getDrawable(R.drawable.ng_background));


            }
        });
        textNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textNew.setTextColor(Color.parseColor("#ffffff"));
                textNew.setBackground(getResources().getDrawable(R.drawable.yes_background));
                textUsed.setTextColor(Color.parseColor("#000000"));
                textUsed.setBackground(getResources().getDrawable(R.drawable.ng_background));
                textAny.setTextColor(Color.parseColor("#000000"));
                textAny.setBackground(getResources().getDrawable(R.drawable.ng_background));


            }
        });
        tvBaY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBaY.setTextColor(Color.parseColor("#ffffff"));
                tvBaY.setBackground(getResources().getDrawable(R.drawable.yes_background));
                tvBaN.setTextColor(Color.parseColor("#000000"));
                tvBaN.setBackground(getResources().getDrawable(R.drawable.ng_background));


            }
        });
        tvBaN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvBaN.setTextColor(Color.parseColor("#ffffff"));
                tvBaN.setBackground(getResources().getDrawable(R.drawable.yes_background));
                tvBaY.setTextColor(Color.parseColor("#000000"));
                tvBaY.setBackground(getResources().getDrawable(R.drawable.ng_background));

            }
        });
        tvIaY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvIaY.setTextColor(Color.parseColor("#ffffff"));
                tvIaY.setBackground(getResources().getDrawable(R.drawable.yes_background));
                tvIaN.setTextColor(Color.parseColor("#000000"));
                tvIaN.setBackground(getResources().getDrawable(R.drawable.ng_background));


            }
        });
        tvIaN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvIaN.setTextColor(Color.parseColor("#ffffff"));
                tvIaN.setBackground(getResources().getDrawable(R.drawable.yes_background));
                tvIaY.setTextColor(Color.parseColor("#000000"));
                tvIaY.setBackground(getResources().getDrawable(R.drawable.ng_background));

            }
        });

    }
    void load_data(){
        // Category
        categoryModelList.add(new CategoryModel(1,R.drawable.cat1,true));
        categoryModelList.add(new CategoryModel(2,R.drawable.cat2,false));
        categoryModelList.add(new CategoryModel(3,R.drawable.cat3,false));
        categoryModelList.add(new CategoryModel(4,R.drawable.cat4,false));
        categoryModelList.add(new CategoryModel(5,R.drawable.cat5,false));
        categories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        catAdapter.submitList(categoryModelList);
        categories.setAdapter(catAdapter);
        // colors
        colors.add(new ColorModel("#ff5e5e", true));
        colors.add(new ColorModel("#ffc453", true));
        colors.add(new ColorModel("#5ae680", false));
        colors.add(new ColorModel("#5ec8ff", false));
        colors.add(new ColorModel("#885eff", true));
        productColors.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        productColors.setAdapter(colorsAdapter);
        colorsAdapter.submitList(colors);

        // Product photo
        photoModelList.add(new PhotoModel(R.drawable.ex));
        photoModelList.add(new PhotoModel(R.drawable.ex));
        photoModelList.add(new PhotoModel(R.drawable.ex));
        photoModelList.add(new PhotoModel(R.drawable.dr_add_photo));
        productsImages.setLayoutManager(new GridLayoutManager(this, 3));
        productsImages.setAdapter(photoAdapter);
        photoAdapter.submitList(photoModelList);

        // Duration
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(
                this,android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.dur)); //Your resource name

        duration.setAdapter(arrayAdapter);

        //Name
        name.setText("V-NECK T-Shirt");
        desc.setText("Short Sleeve T-Shirt\nWith A V-Neck.");
        // Note
        String first = "<font color='#FF5E5E'>Note: </font>";
        String next = "<font color='#000000'> When You Add The Product.\n The Price cannot Be Modified </font>";

        tv_note.setText(Html.fromHtml(first + next));
    }
}