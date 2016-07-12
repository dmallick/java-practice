package com.deb.couchbase;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.search.SearchQueryResult;
import com.couchbase.client.java.search.SearchQueryRow;
import com.couchbase.client.java.search.query.NumericRangeQuery;
import com.couchbase.client.java.search.query.PrefixQuery;
import org.apache.log4j.Logger;

public class BasicRangeAndDate {
    final static Logger logger = Logger.getLogger(BasicRangeAndDate.class);
    public static void main(String[] args) throws InterruptedException {
        new BasicRangeAndDate().search();
        //Thread.sleep(1000);
        System.out.print("Main Thread Existing ...");
    }

    private void search() throws InterruptedException {
        System.setProperty("com.couchbase.queryEnabled", "true");

        Cluster cluster = CouchbaseCluster.create(DefaultCouchbaseEnvironment
                .builder()
               // .autoreleaseAfter(10000)
                .queryEnabled(true)
                .build());

        Bucket bucket = cluster.openBucket("beer-sample");

        SearchQueryResult result = bucket.query(NumericRangeQuery.on("beerIndex")
                .min(3)
                .max(4)
                .field("abv")
                .fields("name", "abv")
                .build());

        System.out.println("\nNumeric Range Query");
        System.out.println("totalHits: " + result.totalHits());
        for (SearchQueryRow row : result) {
            JsonDocument doc = bucket.get(row.id());
            System.out.println("\"" + doc.content().get("name") + "\", abv: " + doc.content().get("abv"));
        }

    }
}
