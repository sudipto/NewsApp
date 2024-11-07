package com.example.sm.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sm on 3/1/18.
 */

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    //  the url string
    private static String mQueryUrl;

    public NewsLoader(Context context, String queryUrl) {
        super(context);
        mQueryUrl = queryUrl;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public ArrayList<News> loadInBackground() {
        //  return null if URL is empty
        if (mQueryUrl == null)
            return null;

        //  create an URL from the string url
        URL url = QueryUtils.createUrl(mQueryUrl);

        //  make the network operation and get the json response in string
        String jsonResponse = null;
        try {
            jsonResponse = QueryUtils.makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("loadInBackground","Error with http request.",e);
        }

        //  parse the json response string and get the list
        ArrayList<News> list = QueryUtils.extractNews(jsonResponse);
        return list;
    }
}
