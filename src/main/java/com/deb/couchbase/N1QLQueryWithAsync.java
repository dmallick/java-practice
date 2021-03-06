package com.deb.couchbase;


import com.couchbase.client.core.CouchbaseException;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.AsyncN1qlQueryRow;
import rx.Observable;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;

public class N1QLQueryWithAsync {

    public static void main(String[] args) throws InterruptedException {
        new N1QLQueryWithAsync().doItInAsync();
        Thread.sleep(1000);
        System.out.print("Main Thread Existing ...");
    }

    private void doItInAsync() throws InterruptedException {
        System.setProperty("com.couchbase.queryEnabled", "true");

        Cluster cluster = CouchbaseCluster.create(DefaultCouchbaseEnvironment
                .builder()
               // .autoreleaseAfter(10000)
                .queryEnabled(true)
                .build());

        Bucket bucket = cluster.openBucket("beer-sample");
        bucket.async()
                .query(select("*").from(i("beer-sample")).limit(10))
                .flatMap(result ->
                        result.errors()
                                .flatMap(e -> Observable.<AsyncN1qlQueryRow>error(new CouchbaseException("N1QL Error/Warning: " + e)))
                                .switchIfEmpty(result.rows())
                )
                .map(AsyncN1qlQueryRow::value)
                .subscribe(
                        rowContent -> System.out.println(rowContent),
                        runtimeError -> runtimeError.printStackTrace()
                );
        System.out.print("Async Thread ...");
    }
}
