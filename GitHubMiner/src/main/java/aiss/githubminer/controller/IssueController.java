package aiss.githubminer.controller;

import aiss.githubminer.service.IssueService;
import aiss.githubminer.model.issue.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/githubminer")

public class IssueController {

    @Autowired
    private IssueService issueService;
    @Autowired
    private RestTemplate restTemplate;
    private final String gitMinerURI = "http://localhost:8080/gitminer"

    @GetMapping("/{owner}/{repoName}")
    public List<Issue> getIssues(@PathVariable String owner,
                               @PathVariable String repoName,
                               @RequestParam int page,
                               @RequestParam int Integer perPage,
                               @RequestParam int Integer nIssues,
                               @RequestParam(defaultValue = "2") Integer sinceIssues,
                               @RequestParam(defaultValue = "2") Integer maxPages) {
    return issueService.getIssues(owner, repoName, page, perPage, nIssues, sinceIssues, maxPages);
    }

    @PostMapping("/{owner}/{repoName}")
    public List<Issue> sendIssues(@PathVariable String owner,
                               @PathVariable String repoName,
                               @RequestParam int page,
                               @RequestParam int Integer perPage,
                               @RequestParam int Integer nIssues,
                               @RequestParam(defaultValue = "2") Integer sinceIssues,
                               @RequestParam(defaultValue = "2") Integer maxPages) {
    List<Issue> issues = issueService.getIssues(owner, repoName, page, perPage, nIssues, sinceIssues, maxPages);
    HttpEntity<List<Issue>> request = new HttpEntity<>(issues);
    ResponseEntity<List<Issue>> response = 
            restTemplate.exchange(gitMinerURI, HttpMethod.POST, request, List.class);
    return response.getBody();
    }


}

