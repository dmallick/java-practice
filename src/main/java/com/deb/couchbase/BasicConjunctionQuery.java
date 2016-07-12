package com.deb.couchbase;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.search.SearchQueryResult;
import com.couchbase.client.java.search.SearchQueryRow;
import com.couchbase.client.java.search.query.ConjunctionQuery;
import com.couchbase.client.java.search.query.MatchQuery;
import com.couchbase.client.java.search.query.RegexpQuery;
import org.apache.log4j.Logger;

public class BasicConjunctionQuery {
    final static Logger logger = Logger.getLogger(BasicConjunctionQuery.class);
    public static void main(String[] args) throws InterruptedException {
        new BasicConjunctionQuery().search();
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

        MatchQuery bitterQuery = MatchQuery.on("beerIndex").match("bitter").field("description").build();
        MatchQuery maltyQuery = MatchQuery.on("beerIndex").match("malty").field("description").build();

        SearchQueryResult result = bucket.query( ConjunctionQuery.on("beerIndex").conjuncts(bitterQuery, maltyQuery) .build());

        System.out.println("\nRegexp Query");
        System.out.println("totalHits: " + result.totalHits());
        for (SearchQueryRow row : result) {
            System.out.println(bucket.get(row.id()).content().get("name"));
        }
    }
}
