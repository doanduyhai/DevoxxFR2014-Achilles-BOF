package info.archinnov.achilles.demo.entity;

import java.util.UUID;
import info.archinnov.achilles.annotations.Column;
import info.archinnov.achilles.annotations.EmbeddedId;
import info.archinnov.achilles.annotations.Entity;
import info.archinnov.achilles.annotations.Order;
import info.archinnov.achilles.demo.model.TweetModel;
import lombok.Data;

@Entity(table = "timeline")
@Data
public class TimeLineEntity {
   /*
    *
    * CREATE TABLE timeline(
    *   user_id bigint,
    *   tweet_id timeuuid,
    *   author text,
    *   content text,
    *   PRIMARY KEY (user_id, tweet_id)
    *);
    */

    @EmbeddedId
    private CompoundKey compoundKey;

    @Column
    private String author;

    @Column
    private String content;


    public TimeLineEntity(Long userId, UUID tweetId, String author, String content) {
        this.compoundKey = new CompoundKey(userId, tweetId);
        this.author = author;
        this.content = content;
    }

    @Data
    public static class CompoundKey {

        @Order(1)
        @Column(name = "user_id")
        private Long userId;

        @Order(2)
        @Column(name = "tweet_id")
        private UUID tweetId;

        public CompoundKey(Long userId, UUID tweetId) {
            this.userId = userId;
            this.tweetId = tweetId;
        }

        public CompoundKey() {

        }
    }

    public TweetModel toModel() {
        return new TweetModel(author, compoundKey.getTweetId(), content);
    }
}
