package info.archinnov.achilles.demo;

import static info.archinnov.achilles.junit.AchillesResourceBuilder.withEntityPackages;
import static org.apache.commons.lang.math.RandomUtils.nextLong;
import static org.fest.assertions.api.Assertions.assertThat;
import java.util.List;
import java.util.UUID;
import org.apache.cassandra.utils.UUIDGen;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import info.archinnov.achilles.demo.entity.TimeLineEntity;
import info.archinnov.achilles.demo.model.TweetModel;
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
        service.manager = manager;
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
        final TweetModel actual = service.findTweet(userId, tweetId);

        //Then
        assertThat(actual).isEqualTo(tweet.toModel());
    }

    @Test
    public void should_get_tweets_from_timeline() throws Exception {
        //Given
        Long userId = nextLong();

        final UUID tweetId1 = service.createTweet(userId, "Jean", "tweet1");
        final UUID tweetId2 = service.createTweet(userId, "Jacques", "tweet2");
        final UUID tweetId3 = service.createTweet(userId, "Pierre", "tweet3");
        final UUID tweetId4 = service.createTweet(userId, "Pierre", "tweet4");
        final UUID tweetId5 = service.createTweet(userId, "Pierre", "tweet5");

        //When
        List<TweetModel> tweets = service.getTweets(userId, tweetId2, 2);

        //Then
        assertThat(tweets).hasSize(2);
        assertThat(tweets.get(0).getCreationDate()).isEqualTo(tweetId3);
        assertThat(tweets.get(1).getCreationDate()).isEqualTo(tweetId4);

        //When
        tweets = service.getTweets(userId, null, 7);

        //Then
        assertThat(tweets).hasSize(5);
        assertThat(tweets.get(0).getCreationDate()).isEqualTo(tweetId1);
        assertThat(tweets.get(1).getCreationDate()).isEqualTo(tweetId2);
        assertThat(tweets.get(2).getCreationDate()).isEqualTo(tweetId3);
        assertThat(tweets.get(3).getCreationDate()).isEqualTo(tweetId4);
        assertThat(tweets.get(4).getCreationDate()).isEqualTo(tweetId5);
    }
}
