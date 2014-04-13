package info.archinnov.achilles.demo;

import static info.archinnov.achilles.junit.AchillesResourceBuilder.withEntityPackages;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import com.datastax.driver.core.Session;
import info.archinnov.achilles.junit.AchillesResource;
import info.archinnov.achilles.persistence.PersistenceManager;

@RunWith(MockitoJUnitRunner.class)
public class TweetServiceTest {

    @Rule
    public AchillesResource resource = withEntityPackages("info.archinnov.achilles.demo.entity")
            .tablesToTruncate("timeline")
            .truncateBeforeAndAfterTest().build();

    private PersistenceManager manager = resource.getPersistenceManager();

    private Session nativeSession = resource.getNativeSession();

    private TweetService service = new TweetService();

    @Before
    public void setUp() {
        service.manager = manager;
    }


}
