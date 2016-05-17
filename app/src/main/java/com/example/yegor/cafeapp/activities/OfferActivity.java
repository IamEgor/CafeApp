package com.example.yegor.cafeapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yegor.cafeapp.R;
import com.example.yegor.cafeapp.Utils;
import com.example.yegor.cafeapp.models.OfferModel;

public class OfferActivity extends BaseActivity {

    private ImageView image;
    private TextView name, weight, price, description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        image = (ImageView) findViewById(R.id.image);
        name = (TextView) findViewById(R.id.name);
        weight = (TextView) findViewById(R.id.weight);
        price = (TextView) findViewById(R.id.price);
        description = (TextView) findViewById(R.id.description);

        OfferModel offer = getIntent().getParcelableExtra(OfferModel.EXTRA);

        Glide.with(this)
                .load(offer.getPicture())
                .fitCenter()
                .placeholder(R.drawable.placeholder)
                .into(image);


        if (offer.getPrice() == null || offer.getPrice().length() == 0)
            name.setVisibility(View.GONE);
        else
            name.setText(String.format(getString(R.string.name), offer.getName()));

        String weightVal = Utils.getParamByName(offer.getParams(), "Вес");
        if (weightVal.length() == 0)
            weight.setVisibility(View.GONE);
        else {
            weight.setText(String.format(getString(R.string.weight), weightVal));
            weight.setVisibility(View.VISIBLE);
        }

        if (offer.getPrice() == null || offer.getPrice().length() == 0)
            price.setVisibility(View.GONE);
        else
            price.setText(String.format(getString(R.string.price), offer.getPrice()));

        if (offer.getDescription() == null || offer.getDescription().length() == 0)
            description.setVisibility(View.GONE);
        else
            description.setText(String.format(getString(R.string.name), offer.getDescription()));

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_offer;
    }

}
