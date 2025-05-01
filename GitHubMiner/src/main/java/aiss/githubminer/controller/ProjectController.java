package aiss.githubminer.controller;

import aiss.githubminer.service.ProjectService;
import aiss.githubminer.model.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @GetMapping("/github/{owner}/{repoName}")
    public Project getProject(@PathVariable String owner, @PathVariable String repoName,
                              @RequestParam(defaultValue = "5") int sinceCommits,
                              @RequestParam(defaultValue = "30") int sinceIssues,
                              @RequestParam(defaultValue = "2") int maxPages) {
        return projectService.getProjectData(owner, repoName);
    }
}

