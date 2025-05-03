package aiss.githubminer.controller;

import aiss.githubminer.service.ProjectService;
import aiss.githubminer.model.project.Project;
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


}

