package com.example.yegor.cafeapp.exceptions;

public class NoConnectionException extends Exception {

    @Override
    public String getMessage() {
        return "No Internet Connection Exception";
    }

}