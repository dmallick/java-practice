package com.deb.couchbase;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.search.SearchQueryResult;
import com.couchbase.client.java.search.SearchQueryRow;
import com.couchbase.client.java.search.query.PrefixQuery;
import com.couchbase.client.java.search.query.RegexpQuery;
import org.apache.log4j.Logger;

public class BasicPrefixQuery {
    final static Logger logger = Logger.getLogger(BasicPrefixQuery.class);
    public static void main(String[] args) throws InterruptedException {
        new BasicPrefixQuery().search();
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

        SearchQueryResult result = result = bucket.query(PrefixQuery.on("beerIndex")
                .prefix("weiss")
                .field("name")
                .build());

        System.out.println("\nPrefix Query");
        System.out.println("totalHits: " + result.totalHits());
        for (SearchQueryRow row : result) {
            System.out.println(bucket.get(row.id()).content().get("name"));
        }

    }
}
