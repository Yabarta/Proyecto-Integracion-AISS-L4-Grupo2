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
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${github.api.url}")
    private String githubApiUrl;

    @Value("${github.token}")
    private String githubToken;
    @Autowired
    private CommitService commitService;
    @Autowired
    private IssueService issueService;

    // (owner, repoName,maxPages, page, perPage, nIssues, sinceIssues, nCommits, sinceCommits)
    public ParsedProject getProjectData(String owner, String repo, Integer maxPages, Integer page, Integer perPage,
                                        Integer nIssues, Integer sinceIssues, Integer nCommits, Integer sinceCommits) {
        // Valores por defecto
        if (sinceIssues == null) {
            sinceIssues = 20;
        }
        if (sinceCommits == null) {
            sinceCommits = 2;
        }
        if (maxPages == null) {
            maxPages = 2;
        }
        if (perPage == null) {
            perPage = 10;
        }

        String url = githubApiUrl + "/" + owner + "/" + repo;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        ResponseEntity<Project> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                org.springframework.http.HttpMethod.GET,
                entity,
                Project.class
        );

        ParsedProject parsedProject = parseProject(response.getBody());

        List<ParsedCommit> commits = commitService.getCommits(owner,repo,page,perPage,nCommits,
                sinceCommits,maxPages);
        List<ParsedIssue> issues = issueService.getIssues(owner,repo,page,perPage,nIssues,
                sinceIssues,maxPages);

        parsedProject.setCommits(commits);
        parsedProject.setIssues(issues);

        return parsedProject;
    }

    public ParsedProject parseProject(Project project) {
        ParsedProject newProject = new ParsedProject(
                String.valueOf(project.getId()),
                project.getName(),
                project.getHtmlUrl()
        );
        return newProject;
    }
}
