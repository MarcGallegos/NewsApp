package com.example.android.newsapp;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class with helper methods to help make HTTP request and parse the JSON response
 */
public final class QueryUtils {

        /** Tag for log messages*/
        private static final String LOG_TAG= QueryUtils.class.getSimpleName();
        /**
         * Create a private constructor because nobody should ever create a {@link QueryUtils} object.
         * This class is only meant to hold static variables and methods, which can be accessed
         * directly from the class name QueryUtils (and an object instance of QueryUtils isn't needed)
         */
        private QueryUtils(){
        }

        /** Query Guardian dataset and return list of {@link NewsEvent} Objects*/
        public static List<NewsEvent> fetchNewsEventData(String requestUrl) {

            //Sleep thread for 3/4's of a second to show progress indicator
            try{
                Thread.sleep(750);
            }catch(InterruptedException ie) {
                Log.e(LOG_TAG,"QueryUtils fetchNewsData: INTERRUPTED...",ie);
                ie.printStackTrace();
            }

            //Create URL object
            URL url = createUrl(requestUrl);

            //Perform HTTP request to the URL and receive JSON response back
            String jsonResponse=null;
            try{
                jsonResponse = makeHttpRequest(url);
                Log.i(LOG_TAG,"QueryUtils fetchNewsData makeHttpRequest CALLED");
            }catch(IOException ioe) {
                Log.e(LOG_TAG,"QueryUtils fetchNewsEventData: Problem making http request", ioe);
            }

            //Extract relevant fields from JSON response and create list of {@link NewsEvent}s
            List<NewsEvent> events = extractFeatureFromJson(jsonResponse);
            //Return list of NewsEvents
            return events;
        }

            /** Returns new URL object from given String URL */
            private static URL createUrl(String stringUrl){
            URL url=null;
            try{
                url=new URL(stringUrl);
            }catch (MalformedURLException mue){
            Log.e(LOG_TAG,"QueryUtils createUrl: Problem building URL",mue);
            }
            return url;
        }

        /** Makes an HTTP Request to given URL and returns jsonResponse string */
        private static String makeHttpRequest(URL url)throws IOException{
            String jsonResponse="";

            //If jsonResponse is null return early
                if (url==null){
                    return jsonResponse;
                }

                HttpURLConnection urlConnection=null;
                InputStream inputStream=null;
                try{
                    urlConnection=(HttpURLConnection)url.openConnection();
                    urlConnection.setReadTimeout(10000 /* milliseconds*/);
                    urlConnection.setConnectTimeout(15000 /* milliseconds*/);
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();

            //If connection is successful (response code 200) Read input stream and parse response
                if (urlConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                    inputStream=urlConnection.getInputStream();
                    jsonResponse=readFromStream(inputStream);
                    Log.i(LOG_TAG,
                            "QueryUtils makeHttpRequest:" +
                                    " SUCCESSFULLY CONNECTED JSON RESPONSE RETRIEVED");
                }else{
                    Log.e(LOG_TAG,"QueryUtils makeHttpRequest:" +
                            " ERROR!!! RESPONSE CODE:" + urlConnection.getResponseCode());
                }
            }catch(IOException e){
                    Log.e(LOG_TAG,"QueryUtils makeHttpRequest:" +
                            " PROBLEM RETRIEVING JSON RESULTS.",e);
                }finally{
                    if (urlConnection!=null){
                        urlConnection.disconnect();
                    }
                    if (inputStream!=null){
            //Closing inputStream could throw IOException, so makeHttpRequest(URL url) specifies
            //an IOException could be thrown.
            inputStream.close();
            }
        }
            return jsonResponse;
    }

        /**Converts the {@link InputStream} into a String, named output, that contains entire JSON
         * response from the server.*/
        private static String readFromStream (InputStream inputStream) throws IOException {
            StringBuilder output=new StringBuilder();
            if (inputStream!=null){
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream,
                        Charset.forName("UTF-8"));
                BufferedReader reader=new BufferedReader(inputStreamReader);
                String line=reader.readLine();
                while(line!=null){
                output.append(line);
                line=reader.readLine();
                }
            }
            return output.toString();
        }

            /**
             * Return a list of {@link NewsEvent) objects that has been built up from parsing
             * features from JSON response
             */
            private static List<NewsEvent> extractFeatureFromJson(String newsEventJSON){
                String authArray;
                //If JSON string is empty or null, return early
                if (TextUtils.isEmpty(newsEventJSON)){
                    return null;
                }

            //Create an empty ArrayList that event parsed list segments can be added to
                List<NewsEvent>events=new ArrayList<>();

                //Try to parse the response string. If there's a problem with the way JSON
                //is formatted, a JSONException exception object will be thrown.
                //Catch exception so app doesn't crash, and print the error message to the logs.
                try {
                    //build a JSON object of NewsEvent features with the corresponding data
                    JSONObject baseJsonResponse = new JSONObject(newsEventJSON);
                    //extract JSONObject with key named "response" (a news event list of results)
                    JSONObject newsEventArticles = baseJsonResponse.getJSONObject("response");

                    //For a given event, extract JSONObject with key named "results"
                    //which represents a list of article data
                    JSONArray article = newsEventArticles.getJSONArray("results");

                    //For each NewsEvent in newsEventArray, create an {@link NewsEvent} object.
                    //while index i is less than array length, increment i..
                    for (int i = 0; i < newsEventArticles.length(); i++) {
                        //extract single newsEvent @ index position (i) from events list
                        JSONObject currentNewsEvent = newsEventArticles.getJSONObject(i);

                        //extract string for key named "sectionName"
                        String sectionName = newsEventArticles.getString("sectionName");
                        //extract string for key named "webTitle"
                        String webTitle = newsEventArticles.getString("webTitle");
                        //extract string for key named "webUrl"
                        String webUrl = newsEventArticles.getString("webUrl");
                        //extract string for key named "webPublicationDate"
                        String webPublicationDate = newsEventArticles.getString("webPublicationDate");
                        //extract string for key named "contributor"
                        //Get JSON object with key named "tags"
                        JSONObject tags = currentNewsEvent.getJSONObject("tags");

                        for(int a=0;a<tags.length();a++){
                            JSONObject authArray=currentNewsEvent.getJSONObject(a);

                            //extract (contributor) string for key named "webTitle"
                            JSONObject webTitleC = authArray.getJSONObject("byline");





                        //Create new {@link NewsEvent} object with sectionName, webTitle,
                        //webUrl, webPublicationDate, and webTitleC (contributor title) from
                        //JSON response
                        NewsEvent newsEvent = new NewsEvent(
                                sectionName, webTitle, webUrl, webPublicationDate, webTitleC);
                        //Add newly created {@link newsEvent} to list of events
                        events.add(newsEvent);
                    }}

                    //If an error is thrown when executing any of the above statements in the "try"
                    // block catch exception here, so the app doesn't crash. Print a log message
                    //with the message from the exception
                } catch (JSONException e){
                    Log.e("QueryUtils","Problem parsing JSON NewsEvent results",e);
                }
                return events;
            }

}
