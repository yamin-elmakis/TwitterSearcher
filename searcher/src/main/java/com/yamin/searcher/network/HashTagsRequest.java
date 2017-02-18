package com.yamin.searcher.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.yamin.searcher.LibKeys;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yamin on 18-Feb-17.
 */
public class HashTagsRequest extends JsonObjectRequest {

    private final String TAG = this.getClass().getSimpleName();
    private String hashtag;

    public HashTagsRequest(String params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) throws UnsupportedEncodingException {
        this(LibKeys.SEARCH_PATH, params, listener, errorListener);
    }

    public HashTagsRequest(String searchPath, String params, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) throws UnsupportedEncodingException {
        super(searchPath + "?q=" + URLEncoder.encode(params, StandardCharsets.UTF_8.toString()), null, listener, errorListener);
        hashtag = params;
        setTag(LibKeys.HASHTAG_REQUEST_TAG);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<>();
        headers.put(LibKeys.STR_AUTHORIZATION, "Bearer " + LibKeys.TWITTER_BEARER);
        return headers;
    }

    @Override
    protected void deliverResponse(JSONObject response) {
        try {
            // get the searched hashtag and add it to the response
            response.put(LibKeys.STR_HASHTAG, hashtag);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.deliverResponse(response);
    }
}