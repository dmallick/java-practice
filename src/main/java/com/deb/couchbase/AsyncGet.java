package com.deb.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dmalli on 6/25/2016.
 */
public class AsyncGet {

    public static void main(String[] args) {
        Cluster cluster = CouchbaseCluster.create();
        final Bucket bucket = cluster.openBucket("ProductService");
        List<JsonDocument> loaded = null;
        while(true) {
            loaded = new ArrayList<JsonDocument>();
            int docsToLoad = 10;
            for (int i = 0; i < docsToLoad; i++) {
                JsonDocument doc = bucket.get("doc-" + i);
                if (doc != null) {
                    loaded.add(doc);
                }
            }
            System.out.print(" Size of document: *************"+ loaded.size());
        }
    }
}
