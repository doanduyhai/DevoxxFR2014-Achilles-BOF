package info.archinnov.achilles.demo;

import static info.archinnov.achilles.demo.entity.TimeLineEntity.CompoundKey;
import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import org.apache.cassandra.utils.UUIDGen;
import info.archinnov.achilles.demo.entity.TimeLineEntity;
import info.archinnov.achilles.demo.model.TweetModel;
import info.archinnov.achilles.persistence.PersistenceManager;

public class TweetService {

    @Inject
    PersistenceManager manager;


    public UUID createTweet(Long userId, String author, String content) {
        final UUID tweetId = UUIDGen.getTimeUUID();
        manager.persist(new TimeLineEntity(userId, tweetId, author, content));
        return tweetId;
    }

    public TweetModel findTweet(Long userId, UUID tweetId) {
        final TimeLineEntity entity = manager.find(TimeLineEntity.class, new CompoundKey(userId, tweetId));
        TweetModel result = null;

        if (entity != null) {
            result = entity.toModel();
        }
        return result;
    }

    public List<TweetModel> getTweets(Long userId, UUID startTweetId, int fetchSize) {
        //TODO return last 'fetchSize' tweets, starting from 'startTweetId' excluded,
        //TODO for a given user
        return null;
    }

}
