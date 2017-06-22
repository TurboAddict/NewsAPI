package com.example.maxru.newsapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private TextView mNewsTextView;
    private SearchView mSearchView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNewsTextView = (TextView) findViewById(R.id.news_data);

        //CharSequence query = mSearchView.getQuery();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
                //findViewById(R.id.action_search_bar).getActionView();
        mSearchView = (SearchView) menu.findItem(R.id.action_search_bar).getActionView();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        CharSequence query = mSearchView.getQuery();
        if(id == R.id.action_search) {
            mNewsTextView.setText("");

            loadNewsData(mSearchView.getQuery().toString());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadNewsData(String location) {
        new NewsTaskQuery().execute(location);
    }

    public class NewsTaskQuery extends AsyncTask<String, Void, String[]> {
        @Override
        protected String[] doInBackground(String... source) {
            if (source.length == 0) {
                return null;
            }

            String publisher = source[0];
            URL newsRequestUrl = NetworkUtils.buildUrl(publisher);

            try {
                String jsonDataResponse = NetworkUtils
                        .getResponseFromHttpUrl(newsRequestUrl);

                JSONObject reader = new JSONObject(jsonDataResponse);
                String articles  = reader.getString("articles");
                JSONArray arr = new JSONArray(articles);

                String[] list = new String[100];
                for(int i = 0; i < arr.length(); i++){
                    list[i] = arr.getJSONObject(i).getString("title");
                }
                return list;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String[] newsData) {
            if(newsData != null) {
                for(String newsString : newsData) {
                    if(newsString != null)
                        mNewsTextView.append((newsString) + "\n\n");
                }
            }
        }
    }
}
