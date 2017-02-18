package com.yamin.searcher.auth;

import com.yamin.searcher.Response;

/**
 * Created by Yamin on 18-Feb-17.
 */
public interface TwitterAuthListener {
    void onAuthentication(@Response int responseCode);
}