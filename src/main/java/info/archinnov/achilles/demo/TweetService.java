package info.archinnov.achilles.demo;

import java.util.List;
import java.util.UUID;
import javax.inject.Inject;
import info.archinnov.achilles.demo.model.TweetModel;
import info.archinnov.achilles.persistence.PersistenceManager;

public class TweetService {

    @Inject
    PersistenceManager manager;


    public UUID createTweet(Long userId, String author, String content) {
        //TODO create tweet given the author and content for the user
        //TODO generate the tweet ID base on time and returns it
        return null;
    }

    public TweetModel findTweet(Long userId, UUID tweetId) {
        //TODO find tweet by id for a given user
        return null;
    }

    public List<TweetModel> getTweets(Long userId, UUID startTweetId, int fetchSize) {
        //TODO return last 'fetchSize' tweets, starting from 'startTweetId' excluded,
        //TODO for a given user
        return null;
    }
}
