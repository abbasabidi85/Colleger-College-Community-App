package com.abs.colleger.app.admin;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class DynamicAdminApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DynamicColors.applyToActivitiesIfAvailable(this);
    }
}
