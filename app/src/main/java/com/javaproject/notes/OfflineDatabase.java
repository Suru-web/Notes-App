package com.javaproject.notes;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

public class OfflineDatabase extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
