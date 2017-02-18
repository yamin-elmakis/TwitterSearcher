package com.yamin.searcher.search;

import android.support.annotation.Nullable;

import com.yamin.searcher.Response;

import org.json.JSONArray;

/**
 * Created by Yamin on 18-Feb-17.
 */
public interface TwitterSearchListener {
    void onSearchResults(@Response int responseCode, @Nullable String hashtag, @Nullable JSONArray tweets);
}