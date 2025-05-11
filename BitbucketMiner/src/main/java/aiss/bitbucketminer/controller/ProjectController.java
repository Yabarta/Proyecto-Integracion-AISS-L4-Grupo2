package aiss.bitbucketminer.controller;

import aiss.bitbucketminer.model.ParsedProject;
import aiss.bitbucketminer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/bitbucket")

public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{workspace}/{repoSlug}")
    public ParsedProject getProject(@PathVariable String workspace,
                                    @PathVariable String repoSlug,
                                    @RequestParam(defaultValue = "5") Integer nCommits,
                                    @RequestParam(defaultValue = "5") Integer nIssues,
                                    @RequestParam(defaultValue = "2") Integer maxPages) {
        return projectService.getProjectData(workspace, repoSlug, maxPages, nCommits, nIssues);
    }

    @PostMapping("/{workspace}/{repoSlug}")
    public ParsedProject sendProject(@PathVariable String workspace,
                                     @PathVariable String repoSlug,
                                     @RequestParam(defaultValue = "5") Integer nCommits,
                                     @RequestParam(defaultValue = "5") Integer nIssues,
                                     @RequestParam(defaultValue = "2") Integer maxPages) {
        ParsedProject project = projectService.getProjectData(workspace, repoSlug, maxPages, nCommits, nIssues);
        HttpEntity<ParsedProject> request = new HttpEntity<>(project);
        String gitMinerURI = "http://localhost:8080/gitminer/projects";
        ResponseEntity<ParsedProject> response =
                restTemplate.exchange(gitMinerURI, HttpMethod.POST, request, ParsedProject.class);
        return response.getBody();
    }

}

