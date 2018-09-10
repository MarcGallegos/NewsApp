package com.example.android.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.AdapterView;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.List;
import java.util.ArrayList;

public class EventActivity extends AppCompatActivity {
    //Tag String for logging purposes
    public static final String LOG_TAG = EventActivity.class.getName();

    /**
     * URL variable for data retrieval from The Guardian dataset
     */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search?q=xbox%2Cplaystation%2Cnintendo%2Cemulator&" +
                    "show-tags=all&api-key=e5904fc3-41f8-4210-a8d5-f67da2cb31b9";

    /**
     * Adapter for NewsEvents list
     */
    private EventAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        // Find reference to {@link ListView} in the layout
        ListView eventListView = (ListView) findViewById(R.id.list);

        //Create new {@Link ArrayAdapter} of events
        mAdapter = new EventAdapter(this, new ArrayList<NewsEvent>());

        //Set adapter to ListView to populate list to user interface
        eventListView.setAdapter(mAdapter);

        //Set onItemListener on ListView, which sends intent to browser
        //to view more details about the selected event from the JSON URL provided in response.
        eventListView.setOnItemClickListener((adapterView, view, position, l) {
            //Find most currently selected event
            NewsEvent currentNewsEvent = mAdapter.getItem(position);
            //Convert string URL into URI Object (to pass Intent constructor)
            Uri newsEventUri = Uri.parse(currentNewsEvent.getUrl());
            //Create new intent to view news event URI
            Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsEventUri);
            //Send intent to launch new browser activity
            startActivity(websiteIntent);
        });

        //Start AsyncTask to fetch NewsEvent data
        NewsEventAsyncTask task = new NewsEventAsyncTask();
        task.execute(GUARDIAN_REQUEST_URL);
    }

    /**
     * define skeleton of AsyncTask as inner class of EarthquakeActivity class
     * <p>
     * {@link AsyncTask} to perform the network request on a background thread, and then
     * update the UI with the list of earthquakes in the response.
     * <p>
     * AsyncTask has three generic parameters: the input type, a type used for progress updates, and
     * an output type. Our task will take a String URL, and return an Earthquake. We won't do
     * progress updates, so the second generic is just Void.
     * <p>
     * We'll only override two of the methods of AsyncTask: doInBackground() and onPostExecute().
     * The doInBackground() method runs on a background thread, so it can run long-running code
     * (like network activity), without interfering with the responsiveness of the app.
     * Then onPostExecute() is passed the result of doInBackground() method, but runs on the
     * UI thread, so it can use the produced data to update the UI.
     */
    private class NewsEventAsyncTask extends AsyncTask<String, Void, List<NewsEvent>> {
        /**
         * This method runs in the BG thread and performs network request
         * we don't perform blocking processes in UI nor do we update the UI from BG threads, so
         * we just return the list of {@link NewsEvent}s as the result
         */
        @Override
        protected List<NewsEvent> doInBackground(String... urls) {
            //Don't perforn request if no URLs, or first URL is null
            if (urls.length < 1 || urls[0] == null) {
                return null;
            }
            List<NewsEvent> result = QueryUtils.fetchNewsEventData(urls[0]);
            return result;
        }

        /**
         * This method runs in the Main (UI) thread after BG work is finished. This method takes
         * as input, the return value of the doInBackground method.
         * First we clear the adapter, to purge previous NewsEvent Guardian Query data
         * Next we update the adapter with the new list of events which triggers the ListView to
         * re-populate with items from list
         */
        @Override
        protected void onPostExecute(List<NewsEvent> data) {
            //Clear adapter of previous news event data
            mAdapter.clear();
            //If a valid list of {@link NewsEvent}s exists, add them to the adapter's dataset.
            //This will trigger the ListView to update.
            if (data != null && !data.isEmpty()) {
                mAdapter.addAll(data);
            }
        }
    }
}
