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

import static com.couchbase.client.java.query.Select.select;
import static com.couchbase.client.java.query.dsl.Expression.i;
import static com.couchbase.client.java.query.dsl.Expression.x;

public class N1QLQuery {

    public static void main(String[] args) {
        System.setProperty("com.couchbase.queryEnabled", "true");

        Cluster cluster = CouchbaseCluster.create(DefaultCouchbaseEnvironment
                .builder()
                .queryEnabled(true)
                .build());

        Bucket bucket = cluster.openBucket("ProductService");
        N1qlQueryResult query = bucket.query(select("*").from("ProductService").limit(10));


        Statement statement = select("fname", "lname", "age").from(i("default")).where(x("age").gt(x("$age")));
        JsonObject placeholderValues = JsonObject.create().put("age", 22);
        N1qlQuery q = N1qlQuery.parameterized(statement, placeholderValues);
        for (N1qlQueryRow row : bucket.query(q)) {
            System.out.println(row);
        }
    }
}
