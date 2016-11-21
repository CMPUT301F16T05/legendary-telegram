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
 * filled in by Randy
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

    /**
     * Task for deleting requests
     * Usage:
     * ElasticSearchController.DeleteRequestsTask deleteRequestsTask = new ElasticSearchController.DeleteRequestsTask();
     * addRequestsTask.execute(deadRequest);
     */
    public static class DeleteRequestsTask extends AsyncTask<Request, Void, Boolean>{

        /**
         * Deletes a request from the ElasticSearch server
         * @param search_params a Vararg of requests
         * @return Boolean indicating whether there was an error.
         * Also sets the ID for the request to null and updates onServer boolean
         */
        @Override
        protected Boolean doInBackground(Request... search_params) {
            verifySettings();

            //Initialized to true but will return false if an error occurs
            Boolean isGood = Boolean.TRUE;

            for (Request request: search_params) {
                Delete delete = new Delete.Builder(request.getId())
                        .index("fa16t5")
                        .type("request")
                        .build();

                try {
                    DocumentResult result = client.execute(delete);
                    if (result.isSucceeded()) {
                        request.setOnServer(Boolean.FALSE);
                        request.setId(null);
                        Log.i("Success", "Deletion yay");
                    }
                    else {
                        isGood = Boolean.FALSE;
                        Log.i("Error", "Elastic search was not able to delete the request.");
                    }
                }
                catch (Exception e) {
                    isGood = Boolean.FALSE;
                    Log.i("Uhoh", "We failed to delete a request fromt elastic search!");
                    e.printStackTrace();
                }
            }

            return isGood;
        }
    }

    /**
     * Task for getting requests
     * Usage:
     * ElasticSearchController.GetRequestsTask getRequestsTask = new ElasticSearchController.GetRequestsTask();
     * requestList = addRequestsTask.execute(queryType, stringsToLookFor).get();
     */
    public static class GetRequests extends AsyncTask<String, Void, ArrayList<Request>> {

        /**
         * Retrieves a request from the ElasticSearch server
         * @param search_params a Vararg of strings. Expects at least two elements,
         *                     first element is expected to be an ElasticSearchQuery constant
         *                     ex "ID", "GEODISTANCE", "SOME OTHER THING THAT MATCHES THE NEW REQUEST FORMAT"
         *                     remaining arguements are the values you are searching for
         * @return an ArrayList<Request>, care should be taken to check that results were actually found
         */
        @Override
        protected ArrayList<Request> doInBackground(String... search_params) {

            if (search_params.length < 2){
                Log.i("Error", "Too few arguements for the get");
                throw new IndexOutOfBoundsException();
            }

            verifySettings();

            ArrayList<Request> requests = new ArrayList<>();
            String query;
            String queryType = search_params[0];


            for (int q=1; q<search_params.length; q++) {
                switch (queryType) {
                    case ElasticSearchQueries.ID:
                        query = "{\"query\": {\"ids\" : {\"type\" : \"request\", \"values\" : [\"" + search_params[q] + "\"]}}}";
                        break;
                    case ElasticSearchQueries.ALL:
                        //NOTE the size can be adjusted to match whatever is desired
                        query = "\"{\"from\": 0, \"size\": 20}";
                        break;
                    case ElasticSearchQueries.GEODISTANCE:
                        //yay for auto parsing
                        query = "{\n" +
                                "        \"filter\" : {\n" +
                                "            \"geo_distance\" : {\n" +
                                "                \"distance\" : \"20km\",\n" +
                                "                \"elasticEnd\" : \""+search_params[q]+"\"\n" +
                                "            }\n" +
                                "        }\n" +
                                "}";
                        break;
                    case ElasticSearchQueries.FEE:
                        //yay for auto parsing
                        query = "{\n" +
                                "    \"query\": {\n" +
                                "        \"range\" : {\n" +
                                "            \"fee\" : {\n" +
                                "                \"gte\" : "+search_params[q]+",\n" +
                                "                \"lte\" : "+search_params[q+1]+"\n" +
                                "            }\n" +
                                "        }\n" +
                                "    }\n" +
                                "}";
                        //Needed because of the way fee queries will be added in
                        q++;
                        break;
                    case ElasticSearchQueries.FEEPERKM:
                        //yay for auto parsing
                        query = "{\n" +
                                "    \"query\": {\n" +
                                "        \"range\" : {\n" +
                                "            \"feeperkm\" : {\n" +
                                "                \"gte\" : "+search_params[q]+",\n" +
                                "                \"lte\" : "+search_params[q+1]+"\n" +
                                "            }\n" +
                                "        }\n" +
                                "    }\n" +
                                "}";
                        //Needed because of the way fee queries will be added in
                        q++;
                        break;
                    default:
                        query = "{\"from\": 0, \"size\": 100, \"query\": {\"match\": {\"" + queryType + "\": \"" + search_params[q] + "\"}}}";
                        break;
                }

                Search search = new Search.Builder(query)
                        .addIndex("fa16t5")
                        .addType("request")
                        .build();
                try {
                    SearchResult result = client.execute(search);
                    if (result.isSucceeded()) {
                        List<Request> foundRequests = result.getSourceAsObjectList(Request.class);
                        requests.addAll(foundRequests);
                        Log.i("Get", "yay");
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
     * Task for adding requests
     * Usage:
     * ElasticSearchController.AddRequestsTask addRequestsTask = new ElasticSearchController.AddRequestsTask();
     * addRequestsTask.execute(newRequest);
     */
    public static class AddRequestsTask extends AsyncTask<Request, Void, Boolean> {
        /**
         * Adds a request to the ElasticSearch server
         * @param requests a Vararg of requests
         * @return Boolean indicating whether the add was successful.
         * Also sets the generated ID for the request and updates onServer boolean
         */
        @Override
        protected Boolean doInBackground(Request... requests) {
            verifySettings();

            //Initialized to true but will return false if an error occurs
            Boolean isGood = Boolean.TRUE;

            for (Request request: requests) {
                //request.setOnServer(Boolean.TRUE);
                Index index = new Index.Builder(request)
                        .index("fa16t5")
                        .type("request")
                        .build();
                try {
                    DocumentResult result = client.execute(index);
                    if (result.isSucceeded()) {
                        request.setOnServer(Boolean.TRUE);
                        request.setId(result.getId());
                    }
                    else {
                        request.setOnServer(Boolean.FALSE);
                        isGood = Boolean.FALSE;
                        Log.i("Error", "Elastic search was not able to add the request.");
                    }
                }
                catch (Exception e) {
                    isGood = Boolean.FALSE;
                    Log.i("Uhoh", "We failed to add a request to elastic search!");
                    e.printStackTrace();
                }
            }

            return isGood;
        }
    }

    /**
     * Task for updating requests
     * Usage:
     * ElasticSearchController.updateRequestsTask updateRequestsTask = new ElasticSearchController.UpdateRequestsTask();
     * addRequestsTask.execute(updatedRequest);
     */
    public static class UpdateRequestsTask extends AsyncTask<Request, Void, Boolean> {

        /**
         * Updates a request already on the server. Does nothing if no ID can be found
         * @param requests a Vararg of requests
         * @return Boolean indicating whether the update was successful.
         * Also sets the generated ID for the request and updates onServer boolean
         */
        @Override
        protected Boolean doInBackground(Request... requests) {

            verifySettings();

            //Initialized to true but will return false if an error occurs
            Boolean isGood = Boolean.TRUE;

            for (Request request: requests) {
                if (request.getId() == null) {
                    Log.i("Error", "Cannot update a request unless it has been added first");
                }else {
                    Index index = new Index.Builder(request)
                            .index("fa16t5")
                            .type("request")
                            .id(request.getId())
                            .build();
                    try {
                        DocumentResult result = client.execute(index);
                        if (result.isSucceeded()) {
                            request.setOnServer(Boolean.TRUE);
                        } else {
                            isGood = Boolean.FALSE;
                            request.setOnServer(Boolean.FALSE);
                            Log.i("Error", "Elastic search was not able to add the request.");
                        }
                    } catch (Exception e) {
                        isGood = Boolean.FALSE;
                        Log.i("Uhoh", "We failed to add a request to elastic search!");
                        e.printStackTrace();
                    }
                }
            }

            return isGood;
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
