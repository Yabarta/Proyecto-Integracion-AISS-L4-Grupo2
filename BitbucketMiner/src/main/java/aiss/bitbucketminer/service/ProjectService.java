package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.ParsedCommit;
import aiss.bitbucketminer.model.ParsedIssue;
import aiss.bitbucketminer.model.ParsedProject;
import aiss.bitbucketminer.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${bitbucket.api.url}")
    private String bitbucketApiUrl;

    @Autowired
    private CommitService commitService;

    @Autowired
    private IssueService issueService;

    public ParsedProject getProjectData(String workspace, String repo_slug,
                                        Integer maxPages, Integer nCommits, Integer nIssues) {

        String url = bitbucketApiUrl + "/" + workspace + "/" + repo_slug;

        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> request = new HttpEntity<>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        ResponseEntity<Project> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                request,
                Project.class
        );

        ParsedProject parsedProject = parseProject(response.getBody());

        List<ParsedCommit> commits = commitService.getCommits(workspace,repo_slug,nCommits,maxPages);
        List<ParsedIssue> issues = issueService.getIssues(workspace,repo_slug,nIssues,maxPages);

        parsedProject.setCommits(commits);
        parsedProject.setIssues(issues);

        return parsedProject;
    }

    public ParsedProject parseProject(Project project) {
        ParsedProject newProject = new ParsedProject(
                String.valueOf(project.getUuid()),
                project.getName(),
                project.getLinks().getHtml().getHref()
        );
        return newProject;
    }
}
