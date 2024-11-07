package com.example.sm.newsapp;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by sm on 25/12/17.
 */

public final class QueryUtils {

    /** Sample JSON response for a news query */
    private static String JSON_RESPONSE;

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<News> extractNews(String jsonResponse) {
        // Save the response
        JSON_RESPONSE = jsonResponse;

        // Create an empty ArrayList that we can start adding news to
        ArrayList<News> news = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of News objects with the corresponding data.

            JSONObject strJsonObject = new JSONObject(jsonResponse);

            JSONObject responseObject = strJsonObject.getJSONObject("response");

            if (responseObject.getString("status").equals("ok")) {

                JSONArray resultsArray = responseObject.getJSONArray("results");

                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject resultsJsonObject = resultsArray.getJSONObject(i);

                    //  get the section, title, URL and date
                    String section = resultsJsonObject.getString("sectionName");
                    String title = resultsJsonObject.getString("webTitle");
                    String url = resultsJsonObject.getString("webUrl");


                    String detailedDate = resultsJsonObject.getString("webPublicationDate");
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
                    StringBuilder date = new StringBuilder();
                    try {
                        Date dateObject = simpleDateFormat.parse(detailedDate);
                        date.append((new SimpleDateFormat("EEE dd, MMM ''yy")).format(dateObject));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    //  get the authors
                    ArrayList<String> authors = new ArrayList<>();
                    JSONArray tags = resultsJsonObject.getJSONArray("tags");
                    if (tags.length() != 0) {
                        for (int j = 0; j < tags.length(); j++) {
                            JSONObject tagObject = tags.getJSONObject(j);
                            authors.add(tagObject.getString("webTitle"));
                        }
                    }

                    //  get the fields
                    JSONObject fields = resultsJsonObject.getJSONObject("fields");
                    String trailText = fields.optString("trailText", null);
                    int wordcount = Integer.parseInt(fields.optString("wordcount","0"));
                    String thumbnailUrl = fields.optString("thumbnail",null);

                    //  add the News object to the arraylist
                    news.add(new News(title, section, authors, date, url, trailText,wordcount,thumbnailUrl));
                }
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of news
        return news;
    }

    /**
     * create the url from the link provided
     */
    public static URL createUrl(String stringUrl) {
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("createUrl","URL not there.",e);
        }

        return url;
    }

    /** extract JSON response in String from the inputstream */
    private static String readJsonResponse(InputStream inputStream)  throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line = bufferedReader.readLine();
            while (line != null) {
                output.append(line);
                line = bufferedReader.readLine();
            }
        }
        return output.toString();
    }

    /** make an http request using an URL} */
    public static String makeHttpRequest(URL url) throws IOException{
        String jsonResponse = "";

        //  return if there is no url
        if (url == null)
            return jsonResponse;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readJsonResponse(inputStream);
            }

        } catch (IOException e) {
            Log.e("makeHttpRequest","Connection problems",e);
        } finally {
            if (inputStream != null)
            /** we have to handle the IOException at the caller method*/
                inputStream.close();
            if (urlConnection != null)
                urlConnection.disconnect();

            return jsonResponse;
        }

    }
}
