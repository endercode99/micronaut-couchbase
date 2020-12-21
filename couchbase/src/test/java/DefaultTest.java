import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Collection;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import io.micronaut.context.ApplicationContext;

import io.micronaut.context.env.PropertySource;
import io.micronaut.core.util.CollectionUtils;

import org.junit.Test;
import util.TestUtil;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

/**
 * Tests related to the default Couchbase configuration.
 *
 * @author Graham Pople
 * @since 1.0.0
 */
public class DefaultTest {
    @Test
    public void injectCluster() throws IOException {
        ApplicationContext applicationContext = ApplicationContext.run();
        Cluster cluster = applicationContext.getBean(Cluster.class);
        assertNotNull(cluster);
    }

    @Test
    public void basicKeyValueOperations() throws IOException, InterruptedException {
        String bucketName = "default";
        String imageName = "couchbase/server";

        ApplicationContext applicationContext = TestUtil.initCouchbaseTestContainer(bucketName, imageName);
        Cluster cluster = applicationContext.getBean(Cluster.class);

        Collection collection = cluster.bucket(bucketName).defaultCollection();

        collection.upsert("id", JsonObject.create().put("foo", "bar"));

        Optional<GetResult> result = Optional.ofNullable(collection.get("id"));

        assertTrue(result.isPresent());
        assertEquals("bar", result.get().contentAs(JsonObject.class).getString("foo"));

    }
}
