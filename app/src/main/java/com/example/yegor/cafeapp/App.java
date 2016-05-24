package com.example.yegor.cafeapp;

import android.app.Application;
import android.content.Context;
import android.os.Build;

import com.joshdholtz.sentry.Sentry;

import org.json.JSONException;

import java.util.Date;

public class App extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;

        Sentry.init(this.getApplicationContext(), getString(R.string.dsn));
        Sentry.setCaptureListener(new Sentry.SentryEventCaptureListener() {

            @Override
            public Sentry.SentryEventBuilder beforeCapture(Sentry.SentryEventBuilder builder) {

                try {
                    builder.getTags().put("system_version", Build.VERSION.RELEASE);
                    builder.getTags().put("device_name", Build.MODEL + ", " + Build.MANUFACTURER);
                    builder.getTags().put("system_date", new Date(Build.TIME).toString());
                } catch (JSONException e) {
                }

                return builder;
            }

        });

    }

    public static Context getContext() {
        return context;
    }

}
