package aiss.githubminer.service;

import aiss.githubminer.model.ParsedCommit;
import aiss.githubminer.model.ParsedIssue;
import aiss.githubminer.model.ParsedProject;
import aiss.githubminer.model.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
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

    @Value("${github.api.url}")
    private String githubApiUrl;

    @Value("${github.token}")
    private String githubToken;

    public ParsedProject getProjectData(String owner, String repo, Integer maxPages, Integer page, Integer perPage,
                                        Integer sinceIssues, Integer sinceCommits) {

        String uri = githubApiUrl + "/" + owner + "/" + repo;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<Project> response = restTemplate.exchange(
                uri,
                org.springframework.http.HttpMethod.GET,
                entity,
                Project.class
        );

        ParsedProject parsedProject = parseProject(response.getBody());

        List<ParsedCommit> commits = commitService.getCommits(owner,repo,page,perPage,sinceCommits,maxPages);
        List<ParsedIssue> issues = issueService.getIssues(owner,repo,page,perPage,sinceIssues,maxPages);

        parsedProject.setCommits(commits);
        parsedProject.setIssues(issues);

        return parsedProject;
    }

    public ParsedProject parseProject(Project project) {
        return new ParsedProject(
                String.valueOf(project.getId()),
                project.getName(),
                project.getHtmlUrl()
        );
    }
}
