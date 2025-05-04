package aiss.bitbucketminer.controller;

import aiss.bitbucketminer.model.Project;
// import aiss.bitbucketminer.service.ProjectService;
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
    private final String gitMinerURI = "http://localhost:8080/gitminer";

    @GetMapping("/{workspace}/{repoSlug}")
    public Project getProject(@PathVariable String workspace,
                              @PathVariable String repoSlug) {
                              //@RequestParam(defaultValue = "5") int sinceCommits,
                              //@RequestParam(defaultValue = "30") int sinceIssues,
                              //@RequestParam(defaultValue = "2") int maxPages)
        return projectService.getProjectData(workspace, repoSlug); // No se si aqui deberiamos meter tambien los parametros opcionales
    }

    @PostMapping("/{workspace}/{repoSlug}")
    public Project sendProject(@PathVariable String workspace,
                               @PathVariable String repoSlug) {
                               // @RequestParam(defaultValue = "5") int sinceCommits,
                               // @RequestParam(defaultValue = "20") int sinceIssues,
                               // @RequestParam(defaultValue = "2") int maxPages)
        Project project = projectService.getProjectData(workspace, repoSlug); // No se si aqui deberiamos meter tambien los parametros opcionales
        HttpEntity<Project> request = new HttpEntity<>(project);
        ResponseEntity<Project> response =
                restTemplate.exchange(gitMinerURI, HttpMethod.POST, request, Project.class);
        return response.getBody();
    }

}

