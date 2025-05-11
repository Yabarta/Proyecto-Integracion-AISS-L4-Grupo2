package aiss.bitbucketminer.controller;

import aiss.bitbucketminer.model.ParsedProject;
import aiss.bitbucketminer.service.ProjectService;
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
@RequestMapping("/bitbucket")
@Tag(name = "Projects", description = "Operaciones GET y POST sobre los proyectos de GitHub")
public class ProjectController {

    @Autowired
    private ProjectService projectService;
    @Autowired
    private RestTemplate restTemplate;

    @Operation(summary = "Obtener el proyecto dado el workspace y el repoSlug")
    @ApiResponse(responseCode = "200", description = "Obtención del proyecto")
    @GetMapping("/{workspace}/{repoSlug}")
    public ParsedProject getProject(@PathVariable String workspace,
                                    @PathVariable String repoSlug,
                                    @RequestParam(defaultValue = "5") Integer nCommits,
                                    @RequestParam(defaultValue = "5") Integer nIssues,
                                    @RequestParam(defaultValue = "2") Integer maxPages) {
        return projectService.getProjectData(workspace, repoSlug, maxPages, nCommits, nIssues);
    }

    @Operation(summary = "Manda el proyecto a crear dado el workspace y el repoSlug")
    @ApiResponse(responseCode = "200", description = "Proyecto enviado")
    @PostMapping("/{workspace}/{repoSlug}")
    public ParsedProject sendProject(@PathVariable String workspace,
                                     @PathVariable String repoSlug,
                                     @RequestParam(defaultValue = "5") Integer nCommits,
                                     @RequestParam(defaultValue = "5") Integer nIssues,
                                     @RequestParam(defaultValue = "2") Integer maxPages) {
        ParsedProject project = projectService.getProjectData(workspace, repoSlug, maxPages, nCommits, nIssues);
        HttpEntity<ParsedProject> request = new HttpEntity<>(project);
        String gitMinerUri = "http://localhost:8080/gitminer/projects";
        ResponseEntity<ParsedProject> response =
                restTemplate.exchange(
                        gitMinerUri,
                        HttpMethod.POST,
                        request,
                        ParsedProject.class
                );
        return response.getBody();
    }

}

