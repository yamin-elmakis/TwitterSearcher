package com.yamin.twittersearcher;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yamin.searcher.Response;
import com.yamin.searcher.TwitterManager;
import com.yamin.searcher.auth.TwitterAuthListener;
import com.yamin.searcher.search.TwitterSearchListener;

import org.json.JSONArray;

import java.io.UnsupportedEncodingException;

/**
 * Created by Yamin on 14-Mar-17.
 */
public class MainActivity extends AppCompatActivity implements
        View.OnClickListener, TwitterAuthListener, TwitterSearchListener {

    private final String TAG = this.getClass().getSimpleName();
    private EditText etSearchKeys;
    private TextView tvHashtag;
    private TweetsAdapter tweetsAdapter;
    private TwitterManager twitterManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        twitterApplicationOnlyAuthentication();

        etSearchKeys = (EditText) findViewById(R.id.main_et_search_keys);
        tvHashtag = (TextView) findViewById(R.id.main_tv_hashtag);
        tweetsAdapter = new TweetsAdapter();
        RecyclerView rvContent = (RecyclerView) findViewById(R.id.main_rv_content);
        rvContent.setHasFixedSize(true);
        rvContent.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvContent.setLayoutManager(layoutManager);
        rvContent.setAdapter(tweetsAdapter);

        findViewById(R.id.main_btn_search).setOnClickListener(this);

        etSearchKeys.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchHashtag();
                    return true;
                }
                return false;
            }
        });
    }


    private void twitterApplicationOnlyAuthentication() {
        try {
            twitterManager = new TwitterManager();
            // edit the KEY and the SECRET in the Twitter Class
            twitterManager.authenticate(Twitter.KEY, Twitter.SECRET, this);
            twitterManager.setSearchListener(this);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.main_btn_search:
                searchHashtag();
                break;
        }
    }

    private void searchHashtag() {
        String searchKey = etSearchKeys.getText().toString();
        if (TextUtils.isEmpty(searchKey)) {
            Toast.makeText(this, getString(R.string.search_toast_prompt), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String hashtag = etSearchKeys.getText().toString().trim();
            twitterManager.search(hashtag);
            etSearchKeys.selectAll();
            Log.d(TAG, "searchHashtag: "+hashtag);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAuthentication(@Response int ResponseCode) {
        if (ResponseCode == Response.RESULT_OK) {
            Toast.makeText(this, getString(R.string.auth_ok), Toast.LENGTH_SHORT).show();
            Log.d(TAG, getString(R.string.auth_ok));
            tvHashtag.setText("");
        } else {
            String error;
            if (TextUtils.isEmpty(Twitter.KEY) || TextUtils.isEmpty(Twitter.SECRET))
                error = getString(R.string.auth_fail2);
            else
                error = getString(R.string.auth_fail);
            Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            tvHashtag.setText(error);
            Log.e(TAG, error);
        }
    }

    @Override
    public void onSearchResults(@Response int responseCode, @Nullable String hashtag, @Nullable JSONArray jsonArray) {
        if (responseCode == Response.RESULT_OK) {
            if (!TextUtils.isEmpty(hashtag))
                tvHashtag.setText(hashtag);
            if (jsonArray != null)
                tweetsAdapter.replaceTweets(jsonArray);
        } else {
            Toast.makeText(this, getString(R.string.search_fail), Toast.LENGTH_SHORT).show();
            Log.e(TAG, getString(R.string.search_fail));
        }
    }
}