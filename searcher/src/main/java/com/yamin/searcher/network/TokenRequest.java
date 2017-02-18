package com.yamin.searcher.network;

import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.yamin.searcher.LibKeys;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Yamin on 18-Feb-17.
 */
public class TokenRequest extends StringRequest {

    private final String TAG = this.getClass().getSimpleName();
    private String key, secret;

    public TokenRequest(String key, String secret, Response.Listener<String> listener, Response.ErrorListener errorListener) throws UnsupportedEncodingException {
        this(key, secret, LibKeys.OAUTH2_PATH, listener, errorListener);
    }

    public TokenRequest(String key, String secret, String path, Response.Listener<String> listener, Response.ErrorListener errorListener) throws UnsupportedEncodingException {
        super(Request.Method.POST, path, listener, errorListener);
        this.key = URLEncoder.encode(key, StandardCharsets.UTF_8.toString());
        this.secret = URLEncoder.encode(secret, StandardCharsets.UTF_8.toString());
        setTag(LibKeys.TOKEN_REQUEST_TAG);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new HashMap<String, String>();
        params.put(LibKeys.STR_GRANT_TYPE, LibKeys.STR_CLIENT_CREDENTIALS);
        return params;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map<String, String> headers = new HashMap<String, String>();
        String auth = "Basic " + Base64.encodeToString((key + ":" + secret).getBytes(), Base64.NO_WRAP);
        headers.put(LibKeys.STR_AUTHORIZATION, auth);
        return headers;
    }
}