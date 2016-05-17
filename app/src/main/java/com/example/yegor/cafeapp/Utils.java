package com.example.yegor.cafeapp;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

import com.example.yegor.cafeapp.models.ParamModel;

import java.util.List;

public class Utils {

    public static String getParamByName(List<ParamModel> params, String paramName) {

        String val = "";

        if (params != null)
            for (ParamModel param : params)
                if (paramName.equals(param.getName()))
                    val = param.getText();

        return val;

    }

    public static boolean hasConnection() {
        ConnectivityManager cm =
                (ConnectivityManager) App.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    public static int dp2pixel(float dp) {
        Resources resources = App.getContext().getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
    }

}
