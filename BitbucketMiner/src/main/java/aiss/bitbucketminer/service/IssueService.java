package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.ParsedComment;
import aiss.bitbucketminer.model.ParsedCommit;
import aiss.bitbucketminer.model.ParsedIssue;
import aiss.bitbucketminer.model.ParsedUser;
import aiss.bitbucketminer.model.issue.Assignee;
import aiss.bitbucketminer.model.issue.Issue;
import aiss.bitbucketminer.model.issue.IssueList;
import aiss.bitbucketminer.model.issue.Reporter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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

    @Value("${bitbucket.api.url}")
    private String bitbucketApiUrl;

    @Autowired
    private CommentService commentService;

    public List<ParsedIssue> getIssues(String workspace, String repo_slug,
                                       Integer nIssues, Integer maxPages) {
        if (maxPages == null) {
            maxPages = 2;
        }

        String url = bitbucketApiUrl + "/" + workspace + "/" + repo_slug + "/issues";

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        ResponseEntity<IssueList> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                IssueList.class
        );

        List<Issue> pagedIssues = new ArrayList<>(response.getBody().getValues());
        pagedIssues = pagedIssues.stream().limit(maxPages * nIssues).toList();
        response.getBody().setValues(pagedIssues);

        List<ParsedIssue> parsedIssues = parseIssues(response.getBody(),workspace,repo_slug);

        return parsedIssues;

    }

    public List<ParsedIssue> parseIssues(IssueList issues, String workspace, String repo_slug) {
        List<ParsedIssue> data = new ArrayList<>();
        List<String> label = new ArrayList<>();
        for (Issue issue : issues.getValues()) {

            label.add(issue.getKind());
            label.add(issue.getPriority());

            ParsedIssue newIssue = new ParsedIssue(
                    String.valueOf(issue.getId()),
                    issue.getTitle(),
                    issue.getContent().getRaw(),
                    issue.getState(),
                    String.valueOf(issue.getCreatedOn()),
                    String.valueOf(issue.getUpdatedOn()),
                    (!(issue.getState().equals("closed"))) ? issue.getUpdatedOn() : null,
                    label,
                    issue.getVotes(),
                    parseAuthor(issue.getReporter()),
                    parseAssignee(issue.getAssignee()));

            List<ParsedComment> comments = commentService.getComments(workspace,repo_slug,newIssue.getId());
            newIssue.setComments(comments);
            data.add(newIssue);
        }
        return data;
    }

    public ParsedUser parseAuthor(Reporter reporter) {
        ParsedUser user = null;
        if (reporter == null) {
            user = new ParsedUser(null, null, null, null, null);
        }
        else {
        user = new ParsedUser(
                String.valueOf(reporter.getUuid()),
                reporter.getNickname(),
                reporter.getDisplayName(),
                reporter.getLinks().getAvatar().getHref(),
                reporter.getLinks().getHtml().getHref());
        }
        return user;
    }

    public ParsedUser parseAssignee(Assignee asignee) {
        ParsedUser user = null;
        if (asignee != null) {
            user = new ParsedUser(
                    String.valueOf(asignee.getUuid()),
                    asignee.getNickname(),
                    asignee.getDisplayName(),
                    asignee.getLinks().getAvatar().getHref(),
                    asignee.getLinks().getHtml().getHref());
        }
        return user;
    }
}
