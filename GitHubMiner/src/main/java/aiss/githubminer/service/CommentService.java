package aiss.githubminer.service;

import aiss.githubminer.model.ParsedComment;
import aiss.githubminer.model.ParsedUser;
import aiss.githubminer.model.comment.Comment;
import aiss.githubminer.model.comment.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${github.api.url}")
    private String githubApiUrl;

    @Value("${github.token}")
    private String githubToken;

    public List<ParsedComment> getComments(String owner, String repo, Long issue, Integer page,
    Integer perPage, Integer nComments, Integer maxPages) {

        List<Comment> allComments = new ArrayList<>();
        int currentPage = (page != null) ? page : 1;

        while (currentPage <= maxPages) {

            String url = githubApiUrl + "/" + owner + "/" + repo + "/issues/" + issue + "/comments";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + githubToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("page", currentPage)
                    .queryParam("per_page", perPage);
            
            System.out.println("URL: " + uriBuilder.toUriString());
            System.out.println("Issue: " + issue);

            ResponseEntity<Comment[]> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    Comment[].class
            );

            List<Comment> comments = Arrays.asList(response.getBody());
            allComments.addAll(comments);

            if (comments.isEmpty()) {
                break;
            }

            currentPage++;
        }

        List<ParsedComment> parsedComments = parseComments(allComments);

        if (nComments != null && nComments < allComments.size()) {
            return parsedComments.subList(0, nComments);
        }

        return parsedComments;
    }

    public List<ParsedComment> parseComments (List<Comment> comments) {
        List<ParsedComment> data = new ArrayList<>();
        for (Comment comment : comments) {
            ParsedComment newComment = new ParsedComment(
                    String.valueOf(comment.getId()),
                    comment.getBody(),
                    parseUser(comment.getUser()),
                    String.valueOf(comment.getCreatedAt()),
                    String.valueOf(comment.getUpdatedAt())
            );
            data.add(newComment);
        }
        return data;
    }

    public ParsedUser parseUser (User user){
        if (user == null) {
            return null;
        }
        ParsedUser newUser = new ParsedUser(
                String.valueOf(user.getId()),
                user.getLogin(),
                user.getLogin(),
                user.getAvatarUrl(),
                user.getHtmlUrl()
        );
        return newUser;
    }
}