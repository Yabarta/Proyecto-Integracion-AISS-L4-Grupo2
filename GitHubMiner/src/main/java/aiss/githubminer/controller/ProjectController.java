package aiss.githubminer.controller;

import aiss.githubminer.model.ParsedProject;
import aiss.githubminer.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.*;

@RestController
@RequestMapping("/github")
@Tag(name = "Projects", description = "Operaciones GET y POST sobre los proyectos de GitHub")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private RestTemplate restTemplate;
    private final String gitMinerUri = "http://localhost:8080/gitminer/projects";

    @Operation(summary = "Obtener el proyecto dado el propietario y el repositorio")
    @ApiResponse(responseCode = "200", description = "Obtención del proyecto")
    @GetMapping("/{owner}/{repoName}")
    public ParsedProject getProject(@PathVariable String owner,
                                    @PathVariable String repoName,
                                    @RequestParam(defaultValue = "2") Integer maxPages,
                                    @RequestParam(required=false) Integer page,
                                    @RequestParam(required=false) Integer perPage,
                                    @RequestParam(defaultValue = "20") Integer sinceIssues,
                                    @RequestParam(defaultValue = "2") Integer sinceCommits){
        return projectService.getProjectData(owner, repoName,maxPages, page, perPage, sinceIssues, sinceCommits);
    }

    @Operation(summary = "Manda el proyecto a crear dado el propietario y el repositorio")
    @ApiResponse(responseCode = "200", description = "Proyecto enviado")
    @PostMapping("/{owner}/{repoName}")
    public ParsedProject sendProject(@PathVariable String owner,
                                     @PathVariable String repoName,
                                     @RequestParam(defaultValue = "2") Integer maxPages,
                                     @RequestParam(required=false) Integer page,
                                     @RequestParam(required=false) Integer perPage,
                                     @RequestParam(defaultValue = "20") Integer sinceIssues,
                                     @RequestParam(defaultValue = "2") Integer sinceCommits) {
        ParsedProject project = projectService.getProjectData(owner, repoName,maxPages, page, perPage,
                sinceIssues, sinceCommits);
        HttpEntity<ParsedProject> request = new HttpEntity<>(project);
        ResponseEntity<ParsedProject> response =
                restTemplate.exchange(gitMinerUri, HttpMethod.POST, request, ParsedProject.class);
        return response.getBody();
    }

}

