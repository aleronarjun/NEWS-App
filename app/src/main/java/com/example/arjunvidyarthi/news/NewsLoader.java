package com.example.arjunvidyarthi.news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class NewsLoader extends AsyncTaskLoader<ArrayList<NewsData>> {

    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<NewsData> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        ArrayList<NewsData> newsList = null;
        try {
            newsList = Utils.networkReq(mUrl);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return newsList;
    }
}