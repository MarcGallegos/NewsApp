package com.example.android.newsapp;

import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.os.Bundle;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;
import java.util.ArrayList;

public class EventActivity extends AppCompatActivity
implements LoaderCallbacks<List<NewsEvent>> {

    /**
     * List empty_state TextView to display if there are no events to display
     */
    private TextView mEmptyStateTextView;

    //Tag String for logging purposes
    public static final String LOG_TAG = EventActivity.class.getName();

    /**
     * URL Constant for data retrieval from The Guardian dataset
     */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search";

    /**
     * Constant value for NewsEvent Loader I.D. Can be any integer as is used for >1 loader
     * logs will bear loader number 42 as practice for logging multiple loaders and
     * as we are using the Guardian API, also as a throwback to Guardians of the Galaxy
     */
    private static final int NEWSEVENT_LOADER_ID = 42;

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

        //Find and display TextView for empty list space if no events found
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        eventListView.setEmptyView(mEmptyStateTextView);

        //Create new {@Link ArrayAdapter} of events taking as input, an empty list of events
        mAdapter = new EventAdapter(this, new ArrayList<NewsEvent>());

        //Set adapter on ListView to populate list to user interface
        eventListView.setAdapter(mAdapter);

        //Set onItemClickListener on ListView, which sends intent to browser
        //to view more details about the selected event from the JSON URL provided in response.
        eventListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Find most currently selected event
                NewsEvent currentNewsEvent = mAdapter.getItem(position);
                //Convert string URL into URI Object (to pass Intent constructor)
                Uri newsEventUri = Uri.parse(currentNewsEvent.getSegmentURL());
                //Create new intent to view NewsEvent URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsEventUri);
                //Send intent to launch new browser activity
                startActivity(websiteIntent);
            }
        });

        //Prepare EventLoader, either via reconnecting to an existing one or start one anew.
        getLoaderManager().initLoader(NEWSEVENT_LOADER_ID, null, this);
        Log.i(LOG_TAG, "TEST: Loader42 INITIALIZED");
    }

    @Override
    public Loader<List<NewsEvent>> onCreateLoader(int i, Bundle bundle) {

        //Log Msg for debugging purposes
        Log.i(LOG_TAG, "TEST: Loader42 onCreateLoader() method CALLED");
        //Create new Loader for the given URL
        SharedPreferences sharedPrefs= PreferenceManager.getDefaultSharedPreferences(this);
        //take user's preference stored in orderBy variable to use as "orderby" parameter
        String orderBy=sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default));

        // pull user prefs from PAGE_SIZE constant variable to use as "page-size" parameter
//        String minEvents=sharedPrefs(R.string.settings_num_of_pgs_key,
//                R.string.settings_order_by_default);
        String minEvents=sharedPrefs.getString(getString(R.string.settings_num_of_pgs_key,
                R.string.settings_order_by_default));

        Uri baseUri=Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder=baseUri.buildUpon();

        //Append Query param & it's respective value.
        uriBuilder.appendQueryParameter("subj-chosen", orderBy);
        uriBuilder.appendQueryParameter("show-fields", "all");
        uriBuilder.appendQueryParameter("page-size", minEvents);
        uriBuilder.appendQueryParameter("api-key", "" );//TODO:<<<<<<ADD TESTERS API-KEY inside" "
        //Create new loader and return completed URI:
        //"https://content.guardianapis.com/search?q=xbox,playstation,nintendo,pc&amp;gaming,pc&amp;
        //games,android,iphone,augmented&amp;reality,virtual&amp;reality&show-fields=all&page-size=
        //200&api-key=TESTER'S API-KEY
        return new EventLoader(this,uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<NewsEvent>> loader, List<NewsEvent> events) {

        //Hide loading indicator as data has been loaded
        View progressBar = findViewById(R.id.indeterminateBar);
        progressBar.setVisibility(View.GONE);

        //Check for Internet Connection
        ConnectivityManager conman =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = conman.getActiveNetworkInfo();
        if (networkInfo == null) {
            //state No Internet Connection
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        } else if (networkInfo != null && networkInfo.isConnected()) {
            //There IS internet, List still empty tho.. state No Events Found
            mEmptyStateTextView.setText(R.string.no_news);
        }

        //Purge adapter of any previous NewsEvent data
        mAdapter.clear();
        //If a valid list of {@link NewsEvent)s exists, add them to adapter's dataset,
        //this will trigger the ListView to update
        if (events != null && !events.isEmpty()) {
            //Add NewsEvent elements to List
            mAdapter.addAll(events);
            Log.i(LOG_TAG, "Test: Loader42 onLoadFinished() method CALLED");
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsEvent>> loader) {
        //Loader RESET to purge data
        mAdapter.clear();
        Log.i(LOG_TAG, "TEST: Loader42 onLoaderReset() method CALLED");
    }

    @Override
    //This method initializes the contents of the Activity's options menu
    public boolean onCreateOptionsMenu(Menu menu){
        //Inflate the options menu listed in main.xml layout
        getMenuInflater().inflate(R.menu.main,menu);
        //Return boolean "true"
        return true;
    }
    @Override
    //pass MenuItem selected
    public boolean onOptionsItemSelected(MenuItem item){
        //returns unique id for menu item defined by @id/ in menu resource
        //determine which item was selected and what action to take
        int id=item.getItemId();
        //menu has one item, @id/action_settings,
        //match id against known menu items to perform appropriate action
        if(id==R.id.action_settings){
            //open SettingsActivity via an intent.
            Intent settingsIntent=new Intent(this,SettingsActivity.class);
            startActivity(settingsIntent);
            //return boolean "true"
            return true;
        }
        //Return item selected.
        return super.onOptionsItemSelected(item);
    }

}