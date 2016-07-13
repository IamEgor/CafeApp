package com.example.yegor.cafeapp.activities;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.example.yegor.cafeapp.R;
import com.example.yegor.cafeapp.Utils;
import com.example.yegor.cafeapp.databinding.ActivityOfferBinding;
import com.example.yegor.cafeapp.models.OfferModel;

public class OfferActivity extends BaseActivity {

    public OfferActivity() {
        super(R.layout.activity_offer);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_left);

        ActivityOfferBinding binding = DataBindingUtil.bind(inflateView);
        binding.setOffer(getIntent().getParcelableExtra(OfferModel.EXTRA));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(v -> {
            if (Utils.isLollipop() && !Utils.isPortrait(this))
                supportFinishAfterTransition();
            else
                onBackPressed();
        });
    }

    @Override
    protected void setStatus(Status status) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        //overridePendingTransition(R.anim.slide_in_left, R.anim.slide_in_right);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
    }
}
