package com.example.yegor.cafeapp.exceptions;

import com.example.yegor.cafeapp.R;
import com.example.yegor.cafeapp.Utils;

public class NoConnectionException extends Exception {

    @Override
    public String getMessage() {
        return Utils.getString(R.string.no_connection_exception);
    }

}