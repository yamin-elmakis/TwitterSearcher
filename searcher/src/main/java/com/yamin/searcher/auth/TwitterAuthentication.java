package com.yamin.searcher.auth;

import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.yamin.searcher.LibKeys;
import com.yamin.searcher.Response;
import com.yamin.searcher.network.MyVolley;
import com.yamin.searcher.network.TokenRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Yamin on 18-Feb-17.
 */
public class TwitterAuthentication implements
        com.android.volley.Response.Listener<String>, com.android.volley.Response.ErrorListener {

    private final String TAG = this.getClass().getSimpleName();
    private TwitterAuthListener authListener;
    private String twitterKey, twitterSecret;
    private boolean cancelOlderRequest;

    public TwitterAuthentication(@NonNull String twitterKey, @NonNull String twitterSecret, @NonNull TwitterAuthListener listener) {
        this.authListener = listener;
        this.twitterKey = twitterKey;
        this.twitterSecret = twitterSecret;
        if (TextUtils.isEmpty(twitterKey) || TextUtils.isEmpty(twitterSecret)) {
            listener.onAuthentication(com.yamin.searcher.Response.PARAM_ERROR);
        }
    }

    public void send() throws UnsupportedEncodingException {
        TokenRequest request = new TokenRequest(twitterKey, twitterSecret, this, this);
        MyVolley.getInstance().addToQueue(request, cancelOlderRequest ? LibKeys.TOKEN_REQUEST_TAG : null);
    }

    public boolean isCancelOlderRequest() {
        return cancelOlderRequest;
    }

    public void setCancelOlderRequest(boolean cancelOlderRequest) {
        this.cancelOlderRequest = cancelOlderRequest;
    }

    @Override
    public void onResponse(String response) {
        Log.d(TAG, "onResponse");
        try {
            JSONObject res = new JSONObject(response);
            if (res.has(LibKeys.STR_TOKEN_TYPE) && res.getString(LibKeys.STR_TOKEN_TYPE).equalsIgnoreCase("bearer")) {
                if (res.has(LibKeys.STR_ACCESS_TOKEN)) {
                    LibKeys.TWITTER_BEARER = res.getString(LibKeys.STR_ACCESS_TOKEN);
                    authListener.onAuthentication(Response.RESULT_OK);
                    Log.d(TAG, "onResponse: bearer: " + LibKeys.TWITTER_BEARER);
                    return;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "onResponse: JSONException: " + e);
        }
        authListener.onAuthentication(Response.RESPONSE_ERROR);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "onErrorResponse: VolleyError: " + error);
        authListener.onAuthentication(Response.SERVER_ERROR);
    }
}