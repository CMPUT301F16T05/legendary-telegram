package com.cmput301fa16t5.legendary_telegram;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import android.os.AsyncTask;
import android.util.Log;

import com.searchly.jestdroid.DroidClientConfig;
import com.searchly.jestdroid.JestClientFactory;
import com.searchly.jestdroid.JestDroidClient;

import java.util.ArrayList;
import java.util.List;

import io.searchbox.client.JestClient;
import io.searchbox.core.Delete;
import io.searchbox.core.DocumentResult;
import io.searchbox.core.Index;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;

/**
 * Created by keith on 11/2/2016.
 * It's the ESearch Controller
 * Gets, Posts, etc to ESearch.
 * Only accessed by the instance of CentralController.
 * Probably where we'll spend a lot of time.
 *
 * Base code adapted from the  lab demonstration code found at
 * https://github.com/SRomansky/lonelyTwitter/blob/lab7end/app/src/main/java/ca/ualberta/cs/lonelytwitter/ElasticsearchTweetController.java
 * from user SRomansky
 */

public class ElasticSearchController {
    private static JestDroidClient client;

    public static class DeleteRequestsTask extends AsyncTask<String, Void, Void>{
        @Override
        protected Void doInBackground(String... search_params) {
            verifySettings();

            for (String params: search_params) {
                Delete delete = new Delete.Builder(params)
                        .index("fa16t5")
                        .type("request")
                        .build();

                try {
                    DocumentResult result = client.execute(delete);
                    if (result.isSucceeded()) {
                        //yay
                    }
                    else {
                        Log.i("Error", "Elastic search was not able to delete the request.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to delete a request fromt elastic search!");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }

    public static class GetRequestsFromIDTask extends AsyncTask<String, Void, ArrayList<Request>> {
        @Override
        protected ArrayList<Request> doInBackground(String... search_params) {
            verifySettings();

            ArrayList<Request> requests = new ArrayList<>();
            String query;

            for (String params: search_params) {
                // assume that search_parameters[0] is the only search term we are interested in using
                //query = params;
                //query = "{\"query\": {\"ids\" : {\"type\" : \"request\", \"values\" : [\"" + params + "]}}}";
                query = "{\"from\": 0, \"size\": 100, \"query\": {\"match\": {\"id\": \"" + params + "\"}}}";

                //query = "{\"query\":{\"ids\":{\"values\":[\"" + params + "\"]}}}";
                Search search = new Search.Builder(query)
                        .addIndex("fa16t5")
                        .addType("request")
                        .build();
                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<Request> foundRequests = result.getSourceAsObjectList(Request.class);
                        requests.addAll(foundRequests);
                        Log.i("Error", "yay");
                    } else {
                        Log.i("Error", "The search query failed to find any requests that matched.");
                    }
                } catch (Exception e) {
                    Log.i("Error", "Something went wrong when we tried to communicate with the elasticsearch server!");
                }
            }
            return requests;
        }
    }
    /**
     * Adds a request to the ElasticSearch server
     * @param userName: The username as a string.
     * @param context: Needed to get file list.
     * @return True of the file exists. False otherwise.
     */

    /**
     * Usage:
     * ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
     * addRequestsTask.execute(newRequest);
     */
    public static class AddRequestsTask extends AsyncTask<Request, Void, Void> {

        @Override
        protected Void doInBackground(Request... requests) {
            verifySettings();

            for (Request request: requests) {
                request.setOnServer(Boolean.TRUE);
                Index index = new Index.Builder(request)
                        .index("fa16t5")
                        .type("request")
                        .id(request.getId())
                        .build();

                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        //yay
                        request.setOnServer(Boolean.TRUE);
                    }
                    else {
                        request.setOnServer(Boolean.FALSE);
                        Log.i("Error", "Elastic search was not able to add the request.");
                    }
                }
                catch (Exception e) {
                    Log.i("Uhoh", "We failed to add a request to elastic search!");
                    e.printStackTrace();
                }
            }

            return null;
        }
    }


    private static void verifySettings() {
        // if the client hasn't been initialized then we should make it!
        if (client == null) {
            DroidClientConfig.Builder builder = new DroidClientConfig.Builder("http://cmput301.softwareprocess.es:8080");
            DroidClientConfig config = builder.build();

            JestClientFactory factory = new JestClientFactory();
            factory.setDroidClientConfig(config);
            client = (JestDroidClient) factory.getObject();
        }
    }

}
