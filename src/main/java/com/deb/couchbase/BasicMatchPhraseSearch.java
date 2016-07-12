package com.deb.couchbase;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.search.SearchQueryResult;
import com.couchbase.client.java.search.SearchQueryRow;
import com.couchbase.client.java.search.query.MatchPhraseQuery;
import com.couchbase.client.java.search.query.MatchQuery;
import com.couchbase.client.java.search.query.SearchQuery;
import org.apache.log4j.Logger;

public class BasicMatchPhraseSearch {
    final static Logger logger = Logger.getLogger(BasicMatchPhraseSearch.class);
    public static void main(String[] args) throws InterruptedException {
        new BasicMatchPhraseSearch().search();
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

        SearchQueryResult result = bucket.query(MatchPhraseQuery.on("beerIndex").matchPhrase("summer seasonal").field("description").build());
        System.out.println("\nFuzzy Match Query");
        System.out.println("totalHits (fuzziness = 2): " + result.totalHits());
        for (SearchQueryRow row : result) {
            System.out.println(bucket.get(row.id()).content().get("name"));
        }
    }
}
