package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.ParsedComment;
import aiss.bitbucketminer.model.ParsedUser;
import aiss.bitbucketminer.model.comment.Comment;
import aiss.bitbucketminer.model.comment.CommentList;
import aiss.bitbucketminer.model.comment.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${bitbucket.api.url}")
    private String bitbucketApiUrl;

    public List<ParsedComment> getComments(String workspace, String repo_slug, String issue_id) {

        String url = bitbucketApiUrl + "/" + workspace + "/" + repo_slug + "/issues/";

        if(issue_id == null){
            url += "comments";
        } else {
            url += issue_id + "/comments";
        }

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        System.out.println("URL: " + uriBuilder.toUriString());
        System.out.println("Issue: " + issue_id);

        ResponseEntity<CommentList> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                org.springframework.http.HttpMethod.GET,
                entity,
                CommentList.class
        );

        List<ParsedComment> parsedComments = new ArrayList<>(parseComments(response.getBody()));
        
        return parsedComments;
    }

    public List<ParsedComment> parseComments (CommentList comments) {
        List<ParsedComment> data = new ArrayList<>();
        for (Comment comment : comments.getValues()) {
            ParsedComment newComment = new ParsedComment(
                    String.valueOf(comment.getId()),
                    comment.getContent().getRaw(),
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
        if (user == null) {
            author = new ParsedUser(null, null, null, null, null);
        }
        else {
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
