package com.deb.couchbase;


import com.couchbase.client.core.CouchbaseException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.AsyncN1qlQueryRow;
import com.couchbase.client.java.search.SearchQueryResult;
import com.couchbase.client.java.search.SearchQueryRow;
import com.couchbase.client.java.search.query.MatchQuery;
import com.couchbase.client.java.search.query.SearchQuery;
import org.apache.log4j.Logger;
import rx.Observable;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;

public class BasicFullTextSearch {
    final static Logger logger = Logger.getLogger(BasicFullTextSearch.class);
    public static void main(String[] args) throws InterruptedException {
        new BasicFullTextSearch().search();
        Thread.sleep(1000);
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
        SearchQueryResult result = bucket.query(ftq);
        System.out.println("totalHits: " + result.totalHits());
        for (SearchQueryRow row : result) {
            System.out.println(row);
        }
    }
}
