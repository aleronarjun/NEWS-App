package com.example.arjunvidyarthi.news;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import java.util.ArrayList;

//https://content.guardianapis.com/search?&api-key=cb250fd8-71ec-4714-b58f-c0fb5208a61b&show-tags=contributor&show-fields=thumbnail -- general query URL.
//https://content.guardianapis.com/search?q=trump&api-key=cb250fd8-71ec-4714-b58f-c0fb5208a61b&show-tags=contributor&show-fields=thumbnail -- specific.

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<NewsData>> {

    String SearchURL = "https://content.guardianapis.com/search?&api-key=cb250fd8-71ec-4714-b58f-c0fb5208a61b&show-tags=contributor&show-fields=thumbnail"; //general.
    NewsAdapter mAdapter;
    ProgressBar progressBar;
    TextView Empty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);


        //Resize search icon:
        final TextView search = (TextView) findViewById(R.id.search_bar);
        final float density = getResources().getDisplayMetrics().density;
        final Drawable drawable_search = getResources().getDrawable(R.drawable.search_icon);
        final int width = Math.round(24*density);
        final int height = Math.round(24*density);

        drawable_search.setBounds(0,0, width, height);
        search.setCompoundDrawables(drawable_search, null, null, null);
        /////////////////////////////////////



        Empty = (TextView) findViewById(R.id.Empty);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        ListView NewsList = (ListView) findViewById(R.id.news_list);
        NewsList.setEmptyView(Empty);
        mAdapter = new NewsAdapter(this, new ArrayList<NewsData>());
        final LoaderManager loaderManager = getLoaderManager();
        NewsList.setAdapter(mAdapter);


        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo nInfo = connMgr.getActiveNetworkInfo();

        if(nInfo==null || !nInfo.isConnected()) {
            mAdapter.clear();
            progressBar.setVisibility(View.GONE);
            Empty.setText("No connection.");
        }

        else {
            loaderManager.initLoader(0, null, this);
        }

        search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keycode, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keycode == KeyEvent.KEYCODE_ENTER)){

                    ConnectivityManager connMgr1 = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo nInfo1 = connMgr1.getActiveNetworkInfo();

                    if(nInfo1 ==null || !nInfo1.isConnected()){
                        mAdapter.clear();
                        Empty.setText("No connection.");
                        return false;
                    }

                    mAdapter.clear();
                    Empty.setVisibility(View.GONE);
                    progressBar.setVisibility(View.VISIBLE);
                    SearchURL = "https://content.guardianapis.com/search?q="+search.getText().toString()+"&api-key=cb250fd8-71ec-4714-b58f-c0fb5208a61b&show-tags=contributor&show-fields=thumbnail";
                    loaderManager.destroyLoader(0);
                    loaderManager.initLoader(0, null, NewsActivity.this);
                    return true;
                }

                else {
                    return false;
                }
            }
        });

        NewsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsData currNews = mAdapter.getItem(i);
                String URL = currNews.returnWebLink();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(URL));
                startActivity(intent);
            }
        });

    }

    @Override
    public Loader<ArrayList<NewsData>> onCreateLoader(int i, Bundle bundle) {
        return new NewsLoader(this, SearchURL);
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<NewsData>> newsList, ArrayList<NewsData> newsDatas) {
        progressBar.setVisibility(View.GONE);
        mAdapter.clear();

        if (newsDatas != null && !newsDatas.isEmpty()) {
            mAdapter.addAll(newsDatas);
        }

        else {
            Empty.setText("No news found for given search...");
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<NewsData>> loader) {
        mAdapter.clear();
    }

}
