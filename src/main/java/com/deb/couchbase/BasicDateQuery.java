package com.deb.couchbase;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.search.SearchQueryResult;
import com.couchbase.client.java.search.SearchQueryRow;
import com.couchbase.client.java.search.query.DateRangeQuery;
import com.couchbase.client.java.search.query.NumericRangeQuery;
import org.apache.log4j.Logger;

import java.util.Calendar;
import java.util.Date;

public class BasicDateQuery {

    final static Logger logger = Logger.getLogger(BasicDateQuery.class);
    public static void main(String[] args) throws InterruptedException {
        new BasicDateQuery().search();
        //Thread.sleep(1000);
        System.out.print("Main Thread Existing ...");
    }

    private void search() throws InterruptedException {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2011, Calendar.MARCH, 1);
        Date start = calendar.getTime();
        calendar.set(2011, Calendar.APRIL, 1);
        Date end = calendar.getTime();

        System.setProperty("com.couchbase.queryEnabled", "true");

        Cluster cluster = CouchbaseCluster.create(DefaultCouchbaseEnvironment
                .builder()
               // .autoreleaseAfter(10000)
                .queryEnabled(true)
                .build());

        Bucket bucket = cluster.openBucket("beer-sample");

        SearchQueryResult result = bucket.query(DateRangeQuery.on("beerIndex")
                .start(start)
                .end(end)
                .field("updated")
                .fields("name", "updated")
                .build());

        System.out.println("\nDate Range Query");
        System.out.println("totalHits: " + result.totalHits());
        for (SearchQueryRow row : result) {
            JsonDocument doc = bucket.get(row.id());
            System.out.println("\"" + doc.content().get("name") + "\", updated: " + doc.content().get("updated"));
        }
    }
}
