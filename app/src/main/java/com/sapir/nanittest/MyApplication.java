package com.sapir.nanittest;

import android.app.Application;

import com.sapir.nanittest.db.Storage;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Storage.getInstance().initialize(this);
    }

}
