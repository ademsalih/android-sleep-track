package com.example.fitbit_tracker;

import android.app.Application;

import com.example.fitbit_tracker.model.User;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmList;

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

        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                User user = realm.where(User.class).findFirst();

                if (user == null) {
                    user = new User();
                    user.setId(1);
                    user.setDob_timestamp(123121230);
                    user.setFirst_name("Adem");
                    user.setLast_name("Salih");
                    user.setHeight(170);
                    user.setWeight(70);
                    user.setNotes("Testing testing ");

                    realm.insert(user);
                }

            }
        });

    }
}
