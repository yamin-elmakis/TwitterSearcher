package com.yamin.twittersearcher;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Yamin on 14-Mar-17.
 */
public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.VHTweet> {

    private static final String STR_NO_NAME = "unknown";
    private static final String STR_NO_TEXT = "<No text>";
    private static final String STR_TWEET_USER = "user";
    private static final String STR_TWEET_NAME = "name";
    private static final String STR_TWEET_TEXT = "text";
    private final String TAG = this.getClass().getSimpleName();
    private JSONArray tweets;

    public TweetsAdapter() {
        this.tweets = new JSONArray();
    }

    @Override
    public VHTweet onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_tweet, parent, false);
        return new VHTweet(view);
    }

    @Override
    public void onBindViewHolder(VHTweet holder, int position) {
        try {
            holder.bind(tweets.getJSONObject(position));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void replaceTweets(JSONArray tweets) {
        // because all the list changes we can replace all the adapter data
        this.tweets = tweets;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.tweets.length();
    }

    public class VHTweet extends RecyclerView.ViewHolder {

        private TextView tvName, tvText;

        public VHTweet(View view) {
            super(view);
            tvName = (TextView) view.findViewById(R.id.item_tweet_name);
            tvText = (TextView) view.findViewById(R.id.item_tweet_text);
        }

        public void bind(JSONObject tweet) {
            if (tweet.length() == 0) {
                tvName.setText(STR_NO_NAME);
                tvText.setText(STR_NO_TEXT);
            } else {
                try {
                    JSONObject user = tweet.getJSONObject(STR_TWEET_USER);
                    tvName.setText(user.optString(STR_TWEET_NAME, STR_NO_NAME));
                } catch (JSONException e) {
                    e.printStackTrace();
                    tvName.setText(STR_NO_NAME);
                }
                tvText.setText(tweet.optString(STR_TWEET_TEXT, STR_NO_TEXT));
            }
        }
    }
}