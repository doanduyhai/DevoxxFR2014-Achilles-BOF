package info.archinnov.achilles.demo;

import static info.archinnov.achilles.demo.entity.TimeLineEntity.CompoundKey;
import static info.archinnov.achilles.junit.AchillesResourceBuilder.withEntityPackages;
import static org.apache.commons.lang.math.RandomUtils.nextLong;
import static org.fest.assertions.api.Assertions.assertThat;
import java.util.UUID;
import org.apache.cassandra.utils.UUIDGen;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import info.archinnov.achilles.demo.entity.TimeLineEntity;
import info.archinnov.achilles.junit.AchillesResource;
import info.archinnov.achilles.persistence.PersistenceManager;

@RunWith(MockitoJUnitRunner.class)
public class TweetServiceTest {

    @Rule
    public AchillesResource resource = withEntityPackages("info.archinnov.achilles.demo.entity").tablesToTruncate("timeline").truncateBeforeAndAfterTest().build();

    private PersistenceManager manager = resource.getPersistenceManager();

    private Session nativeSession = resource.getNativeSession();

    private TweetService service = new TweetService();

    @Before
    public void setUp() {
        Whitebox.setInternalState(service, PersistenceManager.class, manager);
    }

    @Test
    public void should_create_tweet() throws Exception {
        //Given
        Long userId = nextLong();

        //When
        final UUID tweetId = service.createTweet(userId, "Jean", "Ceci est un tweet");

        //Then
        assertThat(tweetId).isNotNull();

        final Row row = nativeSession.execute("SELECT * FROM timeline WHERE user_id=? LIMIT 1", userId).one();

        assertThat(row).isNotNull();
        assertThat(row.getUUID("tweet_id")).isEqualTo(tweetId);
        assertThat(row.getString("author")).isEqualTo("Jean");
        assertThat(row.getString("content")).isEqualTo("Ceci est un tweet");
    }

    @Test
    public void should_find_tweet_by_id() throws Exception {
        //Given
        Long userId = nextLong();
        UUID tweetId = UUIDGen.getTimeUUID();
        final TimeLineEntity tweet = new TimeLineEntity(userId, tweetId, "Jean", "Ceci est un tweet");
        manager.persist(tweet);

        //When
        final TimeLineEntity actual = manager.find(TimeLineEntity.class, new CompoundKey(userId, tweetId));

        //Then
        assertThat(actual).isEqualTo(tweet);
    }
}
