package com.example.fitbit_tracker;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class NyxApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        // Initialize Realm local database at application start.
        // This ensures the init is executed only once for each launch.
        Realm.init(this);

        String realmName = "SessionStore";
        RealmConfiguration config = new RealmConfiguration.Builder().name(realmName).build();

        Realm.setDefaultConfiguration(config);

    }
}
