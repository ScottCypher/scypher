package com.cypher.sffilmfinder.presentation;


import android.app.Application;

import com.cypher.sffilmfinder.BuildConfig;

import timber.log.Timber;

public class SfFilmFinderApplication extends Application {
    private static SfFilmFinderApplication instance;

    private AppComponent appComponent;

    public static SfFilmFinderApplication getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

}
