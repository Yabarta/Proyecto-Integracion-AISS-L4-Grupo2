package aiss.githubminer.service;

import aiss.githubminer.model.ParsedComment;
import aiss.githubminer.model.ParsedIssue;
import aiss.githubminer.model.ParsedUser;
import aiss.githubminer.model.comment.User;
import aiss.githubminer.model.issue.Assignee;
import aiss.githubminer.model.issue.Issue;
import aiss.githubminer.model.issue.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class IssueService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${github.api.url}")
    private String githubApiUrl;

    @Value("${github.token}")
    private String githubToken;
    @Autowired
    private CommentService commentService;

    public List<ParsedIssue> getIssues(String owner, String repo, Integer page, Integer perPage,
     Integer nIssues, Integer sinceIssues, Integer maxPages) {
        // Valores por defecto
        if (sinceIssues == null) {
            sinceIssues = 20; 
        }
        if (maxPages == null) {
            maxPages = 2;
        }
        if (perPage == null) {
            perPage = 10;
        }

        LocalDate sinceDate = LocalDate.now().minusDays(sinceIssues);
        String since = sinceDate.format(DateTimeFormatter.ISO_DATE);

        List<Issue> allIssues = new ArrayList<>();
        int currentPage = (page != null) ? page : 1;

        while (currentPage <= maxPages) {
       
            String url = githubApiUrl + "/" + owner + "/" + repo + "/issues";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + githubToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("since", since)
                    .queryParam("page", currentPage)
                    .queryParam("per_page", perPage);

            System.out.println("URL: " + uriBuilder.toUriString());

            ResponseEntity<Issue[]> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    Issue[].class
            );

            List<Issue> issues = Arrays.asList(response.getBody());
            allIssues.addAll(issues);

            if (issues.isEmpty()) {
                break;
            }

            currentPage++;
        }

        List<ParsedIssue> parsedIssues = parseIssues(allIssues);

        allIssues.forEach(issue -> {
            List<ParsedComment> parsedComments = commentService
            .getComments(owner, repo, issue.getNumber(), null, null, null, null);
            for (ParsedIssue parsed : parsedIssues) {
                if (parsed.getId().equals(String.valueOf(issue.getId()))) {
                    parsed.setComments(parsedComments);
                }
            }
        });

        if (nIssues != null && nIssues < parsedIssues.size()) {
            return parsedIssues.subList(0, nIssues);
        }

        return parsedIssues;

    }

    public List<ParsedIssue> parseIssues(List<Issue> issues) {
        List<ParsedIssue> data = new ArrayList<>();
        List<String> labels = new ArrayList<>();
        for (Issue issue : issues) {
            ParsedIssue newIssue = new ParsedIssue(String.valueOf(issue.getId()),
                    issue.getTitle(),
                    issue.getDescription(),
                    issue.getState(),
                    String.valueOf(issue.getCreatedAt()),
                    String.valueOf(issue.getUpdatedAt()),
                    String.valueOf(issue.getClosedAt()),
                    labels,
                    issue.getVotes().getPlusOne(),
                    parseAuthor(issue.getAuthor()),
                    parseAssignee(issue.getAssignee()));

            List<String> parsedLabels = new ArrayList<>();
            for (Label label : issue.getLabels()) {
                parsedLabels.add(label.getName());
            }
            newIssue.setLabels(parsedLabels);
            data.add(newIssue);
        }
        return data;
    }

    public ParsedUser parseAuthor(User user) {
        if (user == null) {
            return new ParsedUser(null, null, null, null, null); 
        }
        return new ParsedUser(
                String.valueOf(user.getId()),
                user.getLogin(),
                user.getLogin(),
                user.getAvatarUrl(),
                user.getHtmlUrl()
        );
    }
    
    public ParsedUser parseAssignee(Assignee user) {
        if (user == null) {
            return new ParsedUser(null, null, null, null, null); 
        }
        return new ParsedUser(
                String.valueOf(user.getId()),
                user.getLogin(),
                user.getLogin(),
                user.getAvatarUrl(),
                user.getHtmlUrl()
        );
    }
}
