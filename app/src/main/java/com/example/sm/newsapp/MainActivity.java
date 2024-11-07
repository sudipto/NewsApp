package com.example.sm.newsapp;

import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<News>> {

    private ListView listView;
    private NewsAdapter newsAdapter;
    private ConnectivityReceiver receiver;
    private ProgressBar progress;
    private TextView emptyTextView;
    private static int LOADER_ID = 1;
    private static String API_KEY = "10871ca0-716f-47ed-b9d2-4f549076fe29";
    private static String QUERY_URL = "https://content.guardianapis.com/search?orderBy=newest&show-fields=trailText,thumbnail,wordcount&show-tags=contributor&format=json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the ListView from the layout.
        listView = (ListView) findViewById(R.id.listview);

        // Set an empty TextView for the ListView
        emptyTextView = (TextView) findViewById(R.id.empty_view);
        listView.setEmptyView(emptyTextView);

        // Find the progressbar and make it invisible
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);

        newsAdapter = new NewsAdapter(this,new ArrayList<News>());

        // Attach the adapter for the listview.
        listView.setAdapter(newsAdapter);

        //  here we register our receiver for any network connectivity changes
        IntentFilter intentFilter = new IntentFilter();
        receiver = new ConnectivityReceiver();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(receiver,intentFilter);

        //  on any item(article) click event open the link in the browser.
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News item = newsAdapter.getItem(i);
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(item.getUrl()));
                startActivity(intent);
            }
        });
    }

    /**
     * For all the network operations using Loaders.
     */
    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String section = preferences.getString(
                getString(R.string.settings_choose_category_key),
                getString(R.string.settings_choose_category_default)
        );

        Uri baseUri = Uri.parse(QUERY_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("section",section);
        uriBuilder.appendQueryParameter("api-key",API_KEY);

        return new NewsLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> newsArrayList) {

        progress.setVisibility(View.INVISIBLE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyTextView.setText("No news available.");

        newsAdapter.clear();
        if (newsArrayList != null && !newsArrayList.isEmpty())
            newsAdapter.addAll(newsArrayList);
    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        newsAdapter.clear();
    }

    /**
     * Get the LoaderManager and initialize the loader.
     */
    private void startLoading() {
        getLoaderManager().initLoader(LOADER_ID,null,this);
    }

    /**
     * check if network is availble or not
     */
    private boolean isConnected() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    /**
     * broadcast reciever to check if network is available
     */
    private class ConnectivityReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isConnected()) {
                emptyTextView.setVisibility(View.INVISIBLE);
                progress.setVisibility(View.VISIBLE);
                startLoading();
            } else {

                emptyTextView.setText("Please connect to the internet.");
                //  notify the user to that no connectivity available
                Toast.makeText(getApplicationContext(),"No connection available!",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Create an options menu by inflating a menu layout from the Resources.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    /**
     * Action on an Options Item select.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Unregister the reciever here */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
