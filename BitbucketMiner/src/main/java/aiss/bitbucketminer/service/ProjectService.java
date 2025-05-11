package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.ParsedCommit;
import aiss.bitbucketminer.model.ParsedIssue;
import aiss.bitbucketminer.model.ParsedProject;
import aiss.bitbucketminer.model.project.Project__1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    private CommitService commitService;
    @Autowired
    private IssueService issueService;

    @Value("${bitbucket.api.url}")
    private String bitbucketApiUrl;

    public ParsedProject getProjectData(String workspace, String repo_slug,
                                        Integer maxPages, Integer nCommits, Integer nIssues) {

        String uri = bitbucketApiUrl + "/" + workspace + "/" + repo_slug;

        ResponseEntity<Project__1> response =
                restTemplate.exchange(
                    uri,
                    HttpMethod.GET,
                    null,
                    Project__1.class
                );

        ParsedProject parsedProject = parseProject(response.getBody());

        List<ParsedCommit> commits = commitService.getCommits(workspace,repo_slug,nCommits,maxPages);
        List<ParsedIssue> issues = issueService.getIssues(workspace,repo_slug,nIssues,maxPages);

        parsedProject.setCommits(commits);
        parsedProject.setIssues(issues);

        return parsedProject;
    }

    public ParsedProject parseProject(Project__1 project) {
        return new ParsedProject(
                String.valueOf(project.getUuid()),
                project.getName(),
                project.getLinks().getHtml().getHref()
        );
    }
}
