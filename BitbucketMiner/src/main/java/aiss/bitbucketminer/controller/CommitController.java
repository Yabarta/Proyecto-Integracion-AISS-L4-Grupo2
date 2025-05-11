package aiss.bitbucketminer.controller;

import aiss.bitbucketminer.model.Commit;
// import aiss.bitbucketminer.service.CommitService;
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
public class CommitController {

    @Autowired
    private CommitService commitService;
    @Autowired
    private RestTemplate restTemplate;
    private final String gitMinerURI = "http://localhost:8080/gitminer";

    @GetMapping("{workspace}/{repoSlug}/commits")
    public List<Commit> getCommits(@PathVariable String workspace,
                                   @PathVariable String repoSlug,
                                   @RequestParam(required=false) Integer page,
                                   @RequestParam(required=false) Integer perPage,
                                   @RequestParam(required=false) Integer nCommits,
                                   @RequestParam(defaultValue = "2") Integer sinceCommits,
                                   @RequestParam(defaultValue = "2") Integer maxPages) {
        return commitService.getCommits(workspace, repoSlug, page, perPage, nCommits, sinceCommits, maxPages);
    }



}

