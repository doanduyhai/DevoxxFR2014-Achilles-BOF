package info.archinnov.achilles.demo.model;

import java.util.UUID;
import lombok.Data;

@Data
public class TweetModel {

    private String author;
    private UUID creationDate;
    private String content;

    public TweetModel(String author, UUID creationDate, String content) {
        this.author = author;
        this.creationDate = creationDate;
        this.content = content;
    }
}
