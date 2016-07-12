package com.example.yegor.cafeapp.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.yegor.cafeapp.R;
import com.example.yegor.cafeapp.databinding.ActivityOfferBinding;
import com.example.yegor.cafeapp.models.OfferModel;

public class OfferActivity extends BaseActivity {

    public OfferActivity() {
        super(R.layout.activity_offer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityOfferBinding binding = DataBindingUtil.bind(inflateView);
        binding.setOffer( getIntent().getParcelableExtra(OfferModel.EXTRA));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    @Override
    protected void setStatus(Status status) {

    }

}
