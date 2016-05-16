package com.example.yegor.cafeapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.yegor.cafeapp.models.Param;

import java.util.List;

public class Utils {

    public static String getParamByName(List<Param> params, String paramName){

        String val = "";

        if (params != null)
            for (Param param : params)
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


}
