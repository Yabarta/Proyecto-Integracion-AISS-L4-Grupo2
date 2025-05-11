package aiss.githubminer.controller;

import aiss.githubminer.model.ParsedCommit;
import aiss.githubminer.model.commit.Commit;
import aiss.githubminer.service.CommitService;
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
public class CommitController {

    @Autowired
    private CommitService commitService;
    private RestTemplate restTemplate;
    private final String gitMinerURI = "http://localhost:8080/gitminer";

    @GetMapping("/{owner}/{repoName}/commits")
    public List<ParsedCommit> getCommits(@PathVariable String owner,
                                         @PathVariable String repoName,
                                         @RequestParam(required=false) Integer page,
                                         @RequestParam(required=false) Integer perPage,
                                         @RequestParam(required=false) Integer nCommits,
                                         @RequestParam(defaultValue = "2") Integer sinceCommits,
                                         @RequestParam(defaultValue = "2") Integer maxPages) {
        return commitService.getCommits(owner, repoName, page, perPage, nCommits, sinceCommits, maxPages);
    }



}

