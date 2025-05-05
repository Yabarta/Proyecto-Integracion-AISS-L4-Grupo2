package aiss.githubminer.controller;

import aiss.githubminer.model.ParsedProject;
import aiss.githubminer.service.ProjectService;
import aiss.githubminer.model.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/github")

public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private RestTemplate restTemplate;
    private final String gitMinerURI = "http://localhost:8080/gitminer";

    @GetMapping("/{owner}/{repoName}")
    public ParsedProject getProject(@PathVariable String owner,
                                    @PathVariable String repoName,
                                    @RequestParam(defaultValue = "2") Integer maxPages,
                                    @RequestParam(required=false) Integer page,
                                    @RequestParam(required=false) Integer perPage,
                                    @RequestParam(required=false) Integer nIssues,
                                    @RequestParam(defaultValue = "20") Integer sinceIssues,
                                    @RequestParam(required=false) Integer nCommits,
                                    @RequestParam(defaultValue = "2") Integer sinceCommits){
        return projectService.getProjectData(owner, repoName,maxPages, page, perPage, nIssues,
                sinceIssues, nCommits, sinceCommits);
    }

    @PostMapping("/{owner}/{repoName}")
    public ParsedProject sendProject(@PathVariable String owner,
                                     @PathVariable String repoName,
                                     @RequestParam(defaultValue = "2") Integer maxPages,
                                     @RequestParam(required=false) Integer page,
                                     @RequestParam(required=false) Integer perPage,
                                     @RequestParam(required=false) Integer nIssues,
                                     @RequestParam(defaultValue = "20") Integer sinceIssues,
                                     @RequestParam(required=false) Integer nCommits,
                                     @RequestParam(defaultValue = "2") Integer sinceCommits) {
        ParsedProject project = projectService.getProjectData(owner, repoName,maxPages, page, perPage, nIssues,
                sinceIssues, nCommits, sinceCommits);
        HttpEntity<ParsedProject> request = new HttpEntity<>(project);
        ResponseEntity<ParsedProject> response =
                restTemplate.exchange(gitMinerURI, HttpMethod.POST, request, ParsedProject.class);
        return response.getBody();
    }

}

