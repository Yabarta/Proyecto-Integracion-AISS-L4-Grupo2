package aiss.bitbucketminer.controller;

import aiss.bitbucketminer.model.Issue;
// import aiss.bitbucketminer.service.IssueService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/bitbucket")

public class IssueController {

    @Autowired
    private IssueService issueService;
    private RestTemplate restTemplate;
    private final String gitMinerURI = "http://localhost:8080/gitminer";

    @GetMapping("/{workspace}/{repoSlug}/issues")
    public List<Issue> getIssues(@PathVariable String workspace,
                                 @PathVariable String repoSlug,
                                 @RequestParam(required=false) Integer page,
                                 @RequestParam(required=false) Integer perPage,
                                 @RequestParam(required=false) Integer nIssues,
                                 @RequestParam(defaultValue = "20") Integer sinceIssues,
                                 @RequestParam(defaultValue = "2") Integer maxPages) {
    return issueService.getIssues(workspace, repoSlug, page, perPage, nIssues, sinceIssues, maxPages);
    }



}

