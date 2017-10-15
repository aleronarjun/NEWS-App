package com.example.arjunvidyarthi.news;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

/**
 * Created by Arjun Vidyarthi on 13-Oct-17.
 */
public final class Utils {

    private static final String LOG_TAG ="" ;

    private Utils(){
        //Empty constructor so no object is created by mistake...
    }

    public static URL convertToURL(String url){
        URL URL = null;
        try {
            URL = new URL(url);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return URL;
    }

    public static String getFromStream (InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();

    }

    public static String makeHTTPRequest (URL url) throws IOException{
        String jsonResponse = "";

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = getFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    public static ArrayList<NewsData> parseResponse(String JSONResponse) throws JSONException {

        if (TextUtils.isEmpty(JSONResponse)) {
            return null;
        }
        ArrayList<NewsData> newsList = new ArrayList<>();

        try {
            JSONObject baseJsonResponse = new JSONObject(JSONResponse);
            JSONObject response = baseJsonResponse.getJSONObject("response");
            JSONArray resultsArray = response.getJSONArray("results");

            for(int i = 0; i< resultsArray.length();i++){
                String Title, Author;
                JSONObject currentNews = resultsArray.getJSONObject(i);

                String webTitle = currentNews.getString("webTitle");


                try{
                        JSONArray tags = currentNews.getJSONArray("tags");
                        JSONObject currTags = tags.getJSONObject(0);

                        Author = "Posted by "+currTags.getString("firstName") + " " + currTags.getString("lastName");
                    }
                catch(JSONException e){
                        Log.e(LOG_TAG, "no authors found...", e);
                        Author = "Posted by unknown";
                    }
                finally {
                        Title = webTitle;
                    }



                String section = currentNews.getString("sectionName");

                String rawDate = currentNews.getString("webPublicationDate");
                String[] parts2 = rawDate.split("T");
                String date = parts2[0];

                String SectionAndDate = "in "+ section + ", on " + date;

                JSONObject fields = currentNews.getJSONObject("fields");
                String ImgLink = fields.getString("thumbnail");
                Bitmap Img = getBitmapFromURL(ImgLink);

                String WebURL = currentNews.getString("webUrl");

                NewsData news = new NewsData(Title, Author.toUpperCase(), SectionAndDate, Img, WebURL );

                newsList.add(news);
            }
        } catch (JSONException e) {
            Log.e("QueryUtils", "Problem parsing the JSON results", e);
        }

        return newsList;
    }

    public static ArrayList<NewsData> networkReq(String url) throws JSONException {

        Log.e(LOG_TAG, "onNetworkReq");

        URL URL = convertToURL(url);

        String jsonResponse = null;

        try {
            jsonResponse = makeHTTPRequest(URL);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        ArrayList<NewsData> newsList = parseResponse(jsonResponse);

        return newsList;
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception",e.getMessage());
            return null;
        }
    }
}