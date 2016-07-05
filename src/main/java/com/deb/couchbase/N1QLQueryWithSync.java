package com.deb.couchbase;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.Statement;
import rx.Observable;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.x;

public class N1QLQueryWithSync {

    public static void main(String[] args) {
        System.setProperty("com.couchbase.queryEnabled", "true");

        Cluster cluster = CouchbaseCluster.create(DefaultCouchbaseEnvironment
                .builder()
                .queryEnabled(true)
                .build());

        Bucket bucket = cluster.openBucket("beer-sample");
        N1qlQueryResult query = bucket.query(select("*").from("`beer-sample`").limit(10));
        query.allRows().stream().forEach(result -> System.out.println(result.value()));


    }
}
