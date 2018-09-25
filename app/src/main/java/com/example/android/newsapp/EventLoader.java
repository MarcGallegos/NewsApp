package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class EventLoader extends AsyncTaskLoader<List<NewsEvent>> {


    //Tag for Log Messages
    private static final String LOG_TAG = EventLoader.class.getName();

    //Query Url
    private String mUrl;

    /**Constructs new {@link EventLoader}
     * @param context of the activity
     *
     * @param url to load data from
     */
    public EventLoader(Context context,String url){
        super(context);
        mUrl=url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
        Log.i(LOG_TAG,"EventLoader: onStart method CALLED");
    }


    /**This is done in a background thread as we do not perform blocking process in UI thread*/
    @Override
    public List<NewsEvent> loadInBackground(){
        if(mUrl==null){
            Log.i(LOG_TAG,"EventLoader: loadInBackground method STARTED");
            return null;
        }

        //Perform network request, parse the response, and extract list ov NewsEvents
        List<NewsEvent>events=QueryUtils.fetchNewsEventData(mUrl);
        return events;
    }
}
