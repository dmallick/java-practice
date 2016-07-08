package com.deb.couchbase;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.env.DefaultCouchbaseEnvironment;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.couchbase.client.java.query.ParameterizedN1qlQuery;
import com.couchbase.client.java.query.Statement;

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.s;
import static com.couchbase.client.java.query.dsl.Expression.x;
import static com.couchbase.client.java.query.dsl.functions.Case.caseSearch;

public class N1QLQueryParameterized {

    public static void main(String[] args) {
        System.setProperty("com.couchbase.queryEnabled", "true");

        Cluster cluster = CouchbaseCluster.create(DefaultCouchbaseEnvironment
                .builder()
                .queryEnabled(true)
                .build());


        Bucket bucket = cluster.openBucket("beer-sample");
        Statement statement = select("city", "code", "description").from(i("beer-sample")).where(x("city").eq(x("$city")));
        JsonObject placeholderValues = JsonObject.create().put("city", "San Francisco");
        ParameterizedN1qlQuery q = N1qlQuery.parameterized(statement, placeholderValues);
        for (N1qlQueryRow row : bucket.query(q)) {
            System.out.println(row);
        }

    }
}