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
                                    @PathVariable String repoName) {
                              //@RequestParam(defaultValue = "5") int sinceCommits,
                              //@RequestParam(defaultValue = "30") int sinceIssues,
                              //@RequestParam(defaultValue = "2") int maxPages)
        return projectService.getProjectData(owner, repoName); // No se si aqui deberiamos meter tambien los parametros opcionales
    }

    @PostMapping("/{owner}/{repoName}")
    public ParsedProject sendProject(@PathVariable String owner,
                               @PathVariable String repoName) {
                               // @RequestParam(defaultValue = "5") int sinceCommits,
                               // @RequestParam(defaultValue = "20") int sinceIssues,
                               // @RequestParam(defaultValue = "2") int maxPages)
        ParsedProject project = projectService.getProjectData(owner, repoName); // No se si aqui deberiamos meter tambien los parametros opcionales
        HttpEntity<ParsedProject> request = new HttpEntity<>(project);
        ResponseEntity<ParsedProject> response =
                restTemplate.exchange(gitMinerURI, HttpMethod.POST, request, ParsedProject.class);
        return response.getBody();
    }

}

