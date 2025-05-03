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

public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private RestTemplate restTemplate;
    private final String gitMinerURI = "http://localhost:8080/gitminer"

    @GetMapping("/{owner}/{repoName}")
    public Project getProject(@PathVariable String owner,
                              @PathVariable String repoName,
                              @RequestParam(defaultValue = "5") int sinceCommits,
                              @RequestParam(defaultValue = "30") int sinceIssues,
                              @RequestParam(defaultValue = "2") int maxPages) {
        return projectService.getProjectData(owner, repoName); // No se si aqui deberiamos meter tambien los parametros opcionales
    }

    @PostMapping("/{owner}/{repoName}")
    public Project sendProject(@PathVariable String owner,
                               @PathVariable String repoName,
                               @RequestParam(defaultValue = "5") int sinceCommits,
                               @RequestParam(defaultValue = "20") int sinceIssues,
                               @RequestParam(defaultValue = "2") int maxPages) {
        Project project = projectService.getProjectData(owner, repoName); // No se si aqui deberiamos meter tambien los parametros opcionales
        HttpEntity<Project> request = new HttpEntity<>(project);
        ResponseEntity<Project> response =
                restTemplate.exchange(getMinerUri, HttpMethod.POST, request, Project.class);
        return response.getBody();
    }

}

