package com.example.yegor.cafeapp.view;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.yegor.cafeapp.R;

public class CustomImageViewBindingAdapter {

    @BindingAdapter("bind:imageUrl")
    public static void setImage(ImageView image, String imageUrl) {
        if (image != null)
            Glide.with(image.getContext())
                    .load(imageUrl)
                    .fitCenter()
                    .placeholder(R.drawable.placeholder)
                    .into(image);
    }

}
