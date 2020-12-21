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

package io.micronaut.configuration.couchbase;

import com.couchbase.client.core.env.Authenticator;
import com.couchbase.client.core.env.PasswordAuthenticator;
import com.couchbase.client.core.env.SeedNode;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.ClusterOptions;
import com.couchbase.client.java.env.ClusterEnvironment;
import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Primary;
import io.micronaut.context.annotation.Requires;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashSet;

import java.util.Set;

/**
 * Builds the primary Couchbase Cluster.
 *
 * @author Graham Pople
 * @since 1.0
 */
@Requires(classes = Cluster.class)
@Requires(beans = DefaultCouchbaseConfiguration.class)
@Factory
public class DefaultCouchbaseClientFactory {

    /**
     * Factory method to return a Couchbase Cluster.
     * @param configuration an injected DefaultCouchbaseConfiguration
     * @return a Couchbase Cluster
     */
    @Primary
    @Singleton
    Cluster couchbaseCluster(DefaultCouchbaseConfiguration configuration) {
        // Need to destroy env at end
        ClusterEnvironment env = configuration.buildEnvironment();

        Authenticator authenticator = PasswordAuthenticator.create(configuration.username, configuration.password);
        ClusterOptions options = ClusterOptions.clusterOptions(authenticator).environment(env);

        if (configuration.port.kv.isPresent() || configuration.port.http.isPresent()) {
            Set<SeedNode> seedNodes = new HashSet<>(Arrays.asList(
                    SeedNode.create(configuration.uri,
                            configuration.port.kv,
                            configuration.port.http)));
            return Cluster.connect(seedNodes, options);
        } else {
            return Cluster.connect(configuration.uri, options);
        }
    }
}
