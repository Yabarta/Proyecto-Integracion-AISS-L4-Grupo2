package aiss.githubminer.service;

import aiss.githubminer.model.comment.Comment;
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

    public List<Comment> getComments(String owner, String repo, Integer issue, Integer page, 
    Integer perPage, Integer nComments, Integer maxPages) {
        // Valores por defecto
        if (maxPages == null) {
            maxPages = 2;
        }
        if (perPage == null) {
            perPage = 10;
        }

        List<Comment> allComments = new ArrayList<>();
        int currentPage = (page != null) ? page : 1;

        while (currentPage <= maxPages) {

            String url = githubApiUrl + "/" + owner + "/" + repo + "/issues/";

            if(issue == null){
                url += "comments";
            } else {
                url += issue + "/comments";
            }

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + githubToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("page", currentPage)
                    .queryParam("per_page", perPage);

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

        if (nComments != null && nComments < allComments.size()) {
            return allComments.subList(0, nComments);
        }

        return allComments;
    }
}