package com.cmput301fa16t5.legendary_telegram;

/**
 * Created by Randy on 2016-11-10.
 */

public final class ElasticSearchQueries {

    public static final String ID = "id";
    public static final String ALL = "all";
    public static final String GEODISTANCE = "GEODISTANCE QUERY";
    public static final String OTHER ="OTHER";

    private ElasticSearchQueries() { }

    //hiding things in a comment, don't mind me
    /*
    {
        "filter" : {
            "geo_distance" : {
                "distance" : "200km",
                "elasticEnd" : "40,-70"
            }
        }
}

{
    "request" : {
        "properties" : {
            "elasticEnd" : {"type" : "geo_point"}
        }
    }
}

{
        "properties" : {
            "elasticEnd" : {
                "type" : "geo_point"
            }
        }
}
     */
}
