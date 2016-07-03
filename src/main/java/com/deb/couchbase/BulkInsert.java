package com.deb.couchbase;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import rx.Observable;
import rx.functions.Func1;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by dmalli on 6/25/2016.
 *
 */
public class BulkInsert {

    public static void main(String[] args) {
        Cluster cluster = CouchbaseCluster.create();
        final Bucket  bucket = cluster.openBucket("ProductService");
        int docsToCreate = 100;
        List<JsonDocument> documents = new ArrayList<JsonDocument>();
        for (int i = 0; i < docsToCreate; i++) {
            JsonObject content = JsonObject.create()
                    .put("counter", i)
                    .put("name", "Foo Bar");
            documents.add(JsonDocument.create("doc-"+i, content));
        }

      JsonDocument jsonDocument =  Observable
                .from(documents)
                .flatMap(new Func1<JsonDocument, Observable<JsonDocument>>() {
                    @Override
                    public Observable<JsonDocument> call(final JsonDocument docToInsert) {
                        return bucket.async().insert(docToInsert);
                    }
                })
                .last()
                .toBlocking()
                .single();
System.out.print(jsonDocument.toString());
    }


}
