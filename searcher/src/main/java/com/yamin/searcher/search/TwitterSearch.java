package com.yamin.searcher.search;

import android.support.annotation.NonNull;
import android.util.Log;

import com.android.volley.VolleyError;
import com.yamin.searcher.LibKeys;
import com.yamin.searcher.Response;
import com.yamin.searcher.network.HashTagsRequest;
import com.yamin.searcher.network.MyVolley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by Yamin on 18-Feb-17.
 */
public class TwitterSearch implements
        com.android.volley.Response.Listener<JSONObject>, com.android.volley.Response.ErrorListener{

    private final String TAG = this.getClass().getSimpleName();

    private TwitterSearchListener searchListener;
    private boolean cancelOlderRequest;

    public TwitterSearch(@NonNull TwitterSearchListener searchListener) {
        this.searchListener = searchListener;
    }

    public void search(String hashTag) throws UnsupportedEncodingException {
        HashTagsRequest request = new HashTagsRequest(hashTag, this, this);
        MyVolley.getInstance().addToQueue(request, cancelOlderRequest ? LibKeys.HASHTAG_REQUEST_TAG : null);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(TAG, "onErrorResponse: VolleyError: " + error);
        searchListener.onSearchResults(Response.SERVER_ERROR, null, null);
    }

    @Override
    public void onResponse(JSONObject response) {
        String hastag = null;
            try {
                // get the searched hashtag from the response
                if (response.has(LibKeys.STR_HASHTAG))
                    hastag = response.getString(LibKeys.STR_HASHTAG);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        try {
            if (response.has(LibKeys.STR_STATUSES)) {
                JSONArray tweets = response.getJSONArray(LibKeys.STR_STATUSES);
                searchListener.onSearchResults(Response.RESULT_OK, hastag, tweets);
                return;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        searchListener.onSearchResults(Response.RESPONSE_ERROR, hastag, null);
    }

    public boolean isCancelOlderRequest() {
        return cancelOlderRequest;
    }

    public void setCancelOlderRequest(boolean cancelOlderRequest) {
        this.cancelOlderRequest = cancelOlderRequest;
    }

    public TwitterSearchListener getSearchListener() {
        return searchListener;
    }

    public void setSearchListener(@NonNull TwitterSearchListener searchListener) {
        this.searchListener = searchListener;
    }
}