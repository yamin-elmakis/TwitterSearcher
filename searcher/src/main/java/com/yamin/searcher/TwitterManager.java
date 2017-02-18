package com.yamin.searcher;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.yamin.searcher.auth.TwitterAuthListener;
import com.yamin.searcher.auth.TwitterAuthentication;
import com.yamin.searcher.network.MyVolley;
import com.yamin.searcher.search.TwitterSearch;
import com.yamin.searcher.search.TwitterSearchListener;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 * Created by Yamin on 18-Feb-17.
 */
public class TwitterManager implements TwitterAuthListener {

    private static TwitterManager instance = new TwitterManager();
    private final String TAG = this.getClass().getSimpleName();
    private TwitterAuthListener authListener;
    private TwitterSearchListener searchListener;
    private ArrayList<String> pendingHashtags;
    private TwitterSearch twitterSearch;

    private TwitterManager() {  /* empty */ }

    public static TwitterManager getInstance() {
        return instance;
    }

    public void init(@NonNull Context context) {
        MyVolley.init(context);
    }

    public void authenticate(@NonNull String twitterKey, @NonNull String twitterSecret, @NonNull TwitterAuthListener listener) throws UnsupportedEncodingException {
        this.authListener = listener;
        TwitterAuthentication auth = new TwitterAuthentication(twitterKey, twitterSecret, this);
        auth.send();
    }

    public void search(TwitterSearchListener searchListener, String hashtag) throws UnsupportedEncodingException {
        this.searchListener = searchListener;
        search(hashtag);
    }

    public void search(String hashtag) throws UnsupportedEncodingException, IllegalStateException {
        if (searchListener == null) {
            throw new IllegalStateException("TwitterSearchListener not initialized");
        }
        // if we didn't got the bearer from the server yet
        if (TextUtils.isEmpty(LibKeys.TWITTER_BEARER)) {
            if (pendingHashtags == null)
                pendingHashtags = new ArrayList<>();
            pendingHashtags.add(hashtag);
        } else {
            if (twitterSearch == null)
                twitterSearch = new TwitterSearch(searchListener);
            twitterSearch.search(hashtag);
        }
    }

    @Override
    public void onAuthentication(@Response int resultCode) {
        authListener.onAuthentication(resultCode);

        // if the user searched some hashtags before the end of the Authentication
        // then send them now
        if (resultCode != Response.RESULT_OK) return;

        if (pendingHashtags == null || pendingHashtags.isEmpty()) return;

        for (String hashtag : pendingHashtags) {
            try {
                search(hashtag);
            } catch (UnsupportedEncodingException e) {
                if (searchListener != null)
                    searchListener.onSearchResults(Response.PARAM_ERROR, hashtag, null);
            }
        }
    }

    public void setSearchListener(@NonNull TwitterSearchListener searchListener) {
        this.searchListener = searchListener;
        if (twitterSearch != null)
            twitterSearch.setSearchListener(searchListener);
    }
}
