package com.deb.couchbase;


import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.view.ViewQuery;
import com.couchbase.client.java.view.ViewResult;
import com.couchbase.client.java.view.ViewRow;

public class QueryView {

    public static void main(String[] args) {
        Cluster cluster = CouchbaseCluster.create();
        final Bucket bucket = cluster.openBucket("ProductService");
        // Perform the ViewQuery
        ViewResult result = bucket.query(ViewQuery.from("all_products", "all_products").development(false).limit(10));

        // Iterate through the returned ViewRows
        for (ViewRow row : result) {
            System.out.println(row);
        }
    }
}
