/*
 * Copyright 2017-2019 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import io.micronaut.context.ApplicationContext;

/**
 * A simple example of using Couchbase and Micronauts together.
 *
 * Please change application.yml to reflect your Couchbase Cluster configuration.
 */
public class Example {
    /**
     * The main method.
     * @param args any command-line args are ignored, configuration is done with application.yml
     */
    public static void main(String[] args) {
        // Setup Micronauts
        ApplicationContext applicationContext = ApplicationContext.run("test");

        // Get a Couchbase Cluster.  This will be configured according to "couchbase" parameters in application.yml.
        Cluster cluster = applicationContext.getBean(Cluster.class);

        // Access a Couchbase bucket resource named "default".
        Bucket bucket = cluster.bucket("default");

        // Access a Couchbase default collection".
        Collection collection = bucket.defaultCollection();

        // Create and Upsert a JSON document key "id" (upsert does an insert if it's not there, or a replace if it is)
        collection.upsert("id", JsonObject.create().put("foo", "bar"));

        // Read back the document
        GetResult result = collection.get("id");

        // Check the document has the expected content
        assert (result.contentAsObject().getString("name").equals("bar"));
    }
}
