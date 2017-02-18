package com.yamin.searcher.network;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

/**
 * Created by Yamin on 18-Feb-17.
 */
public class MyVolley {

    private final String TAG = this.getClass().getSimpleName();
    private static RequestQueue mRequestQueue;
    private static MyVolley instance;

    private MyVolley() {    /* empty */     }

    public static MyVolley getInstance() {
        if (instance == null)
            instance = new MyVolley();
        return instance;
    }

    public static void init(Context context) {
        if (mRequestQueue == null)
            mRequestQueue = Volley.newRequestQueue(context);
    }

    private static RequestQueue getRequestQueue() {
        if (mRequestQueue != null) {
            return mRequestQueue;
        } else {
            throw new IllegalStateException("MyVolley RequestQueue not initialized");
        }
    }

    public void addToQueue(@NonNull StringRequest request) {
        addToQueue(request, null);
    }

    public void addToQueue(@NonNull StringRequest request, @Nullable String tagToCancel) {
        RequestQueue queue = MyVolley.getRequestQueue();
        if (!TextUtils.isEmpty(tagToCancel))
            queue.cancelAll(tagToCancel);
        queue.add(request);
    }

    public void addToQueue(@NonNull JsonRequest<JSONObject> request) {
        addToQueue(request, null);
    }

    public void addToQueue(@NonNull JsonRequest<JSONObject> request, @Nullable String tagToCancel) {
        RequestQueue queue = MyVolley.getRequestQueue();
        if (!TextUtils.isEmpty(tagToCancel))
            queue.cancelAll(tagToCancel);
        queue.add(request);
    }
}