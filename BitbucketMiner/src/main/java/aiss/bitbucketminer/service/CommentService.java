package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.ParsedComment;
import aiss.bitbucketminer.model.ParsedUser;
import aiss.bitbucketminer.model.comment.Comment;
import aiss.bitbucketminer.model.comment.CommentList;
import aiss.bitbucketminer.model.comment.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${bitbucket.api.url}")
    private String bitbucketApiUrl;

    public List<ParsedComment> getComments(String workspace, String repo_slug, String issue_id) {

        String uri = bitbucketApiUrl + "/" + workspace + "/" + repo_slug + "/issues/" + issue_id + "/comments" ;

        ResponseEntity<CommentList> response = restTemplate.exchange(
                uri,
                org.springframework.http.HttpMethod.GET,
                null,
                CommentList.class
        );

        return parseComments(response.getBody());
    }

    public List<ParsedComment> parseComments (CommentList comments) {
        List<ParsedComment> data = new ArrayList<>();
        for (Comment comment : comments.getValues()) {
            String body = comment.getContent().getHtml().isEmpty() ? "[No message]" : comment.getContent().getHtml();
            ParsedComment newComment = new ParsedComment(
                    String.valueOf(comment.getId()),
                    body,
                    parseAuthor(comment.getUser()),
                    String.valueOf(comment.getCreatedOn()),
                    String.valueOf(comment.getUpdatedOn())
            );
            data.add(newComment);
        }
        return data;
    }

    public ParsedUser parseAuthor(User user) {
        ParsedUser author = null;
        if (user != null) {
            author = new ParsedUser(
                    String.valueOf(user.getUuid()),
                    user.getNickname(),
                    user.getDisplayName(),
                    user.getLinks().getAvatar().getHref(),
                    user.getLinks().getHtml().getHref());
        }
        return author;
    }
}
