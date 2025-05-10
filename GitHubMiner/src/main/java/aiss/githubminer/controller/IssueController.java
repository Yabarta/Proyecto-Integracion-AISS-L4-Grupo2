package aiss.githubminer.controller;

import aiss.githubminer.model.ParsedIssue;
import aiss.githubminer.model.issue.Issue;
import aiss.githubminer.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/github")

public class IssueController {

    @Autowired
    private IssueService issueService;
    private RestTemplate restTemplate;
    private final String gitMinerURI = "http://localhost:8080/gitminer";

    @GetMapping("/{owner}/{repoName}/issues")
    public List<ParsedIssue> getIssues(@PathVariable String owner,
                                       @PathVariable String repoName,
                                       @RequestParam(required=false) Integer page,
                                       @RequestParam(required=false) Integer perPage,
                                       @RequestParam(required=false) Integer nIssues,
                                       @RequestParam(defaultValue = "20") Integer sinceIssues,
                                       @RequestParam(defaultValue = "2") Integer maxPages) {
    return issueService.getIssues(owner, repoName, page, perPage, nIssues, sinceIssues, maxPages);
    }


}

