package com.example.android.newsapppk;

import android.nfc.Tag;
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

import javax.net.ssl.HttpsURLConnection;

public class QueryUtils {

    private static final String TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {}

    public static List<News> fetchNewsData(String requestedURL) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ie) {
            Log.e(TAG, "fetchNewsData: Interrupted ", ie);
        }

        //Create URL
        URL newsURL = createURL(requestedURL);

        //Perform HTTP request
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(newsURL);
        } catch (IOException ioe) {
            Log.e(TAG, "fetchNewsData: Problem making HTTP request", ioe);
        }

        //Extract relevant data
        List<News> myNews = extractNewsFromJson(jsonResponse);
        
        return myNews;
        }

    private static List<News> extractNewsFromJson(String jsonResponse) {
        String title;
        String author;
        String date;
        String urlSource;
        //check for json if null
        if (TextUtils.isEmpty(jsonResponse)) {
            return null;
        }
        List<News> myNews = new ArrayList<>();
        try {
            JSONObject baseJSONResponse = new JSONObject(jsonResponse);

            JSONObject baseJSONEResponseResult = baseJSONResponse.getJSONObject("response");

            JSONArray currentNewsArticles = baseJSONEResponseResult.getJSONArray("results");
            for (int n = 0; n < currentNewsArticles.length(); n++) {
                JSONObject currentArticle = currentNewsArticles.getJSONObject(n);

                title = currentArticle.getString("webTitle");
                urlSource = currentArticle.getString("webURL");
                date = currentArticle.getString("webPublicationDate");

                JSONArray authorArray = currentArticle.getJSONArray("tags");
            }

        } catch (JSONException je) {
            Log.e(TAG, "extractNewsFromJson: Problem Parsing results", je);
        }
        return myNews;
    }

    private static String makeHttpRequest(URL newsURL) throws IOException {
        String jsonResponse = "";
        //Check for null
        if (newsURL == null) {
            return jsonResponse;
        }
        HttpsURLConnection urlConnection = null;
        InputStream inputStream = null;
        //Create the connection
        try {
            urlConnection = (HttpsURLConnection) newsURL.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("Get");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            }else {
                Log.e(TAG, "makeHTTPRequest: Error Code: " + urlConnection.getResponseCode());
            }
        } catch (IOException ioe){
            Log.e(TAG, "makeHTTPRequest: Couldn't retrieve json", ioe );
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }   if (inputStream != null) {
            inputStream.close();
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
            return output.toString();
        }


        private static URL createURL (String requestedURL){
            URL url = null;
            try {
                url = new URL(requestedURL);
            } catch (MalformedURLException mue) {
                Log.e(TAG, "createURL: Problem building URL", mue);
            }
            return url;

        }
    }