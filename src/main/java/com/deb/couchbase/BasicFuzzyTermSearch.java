package com.deb.couchbase;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.search.SearchQueryResult;
import com.couchbase.client.java.search.SearchQueryRow;
import com.couchbase.client.java.search.query.MatchQuery;
import com.couchbase.client.java.search.query.SearchQuery;
import org.apache.log4j.Logger;

public class BasicFuzzyTermSearch {
    final static Logger logger = Logger.getLogger(BasicFuzzyTermSearch.class);
    public static void main(String[] args) throws InterruptedException {
        new BasicFuzzyTermSearch().search();
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
        SearchQuery ftq = MatchQuery.on("beerIndex").match("country").limit(3).build();
        SearchQueryResult result = result = bucket.query(MatchQuery.on("beerIndex")
                .match("sammar")
                .field("name")
                .fuzziness(2) //actually the default
                .build());

        System.out.println("\nFuzzy Match Query");
        System.out.println("totalHits (fuzziness = 2): " + result.totalHits());
        for (SearchQueryRow row : result) {
            System.out.println(bucket.get(row.id()).content().get("name"));
        }
    }
}
