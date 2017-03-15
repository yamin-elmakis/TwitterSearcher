package com.yamin.twittersearcher;

import android.app.Application;

import com.yamin.searcher.TwitterManager;

/**
 * Created by Yamin on 14-Mar-17.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TwitterManager.init(this);
    }
}
