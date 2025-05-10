package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/projects")
public class ProjectController {

    @Autowired
    ProjectRepository repository;

    //GET http://localhost:8080/gitminer/projects
    @GetMapping
    public List<Project> findAll() {
        return repository.findAll();
    }

    //GET http://localhost:8080/gitminer/projects/{id}
    @GetMapping("/{id}")
    public Project findOne(@PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = repository.findById(id);
        if (!project.isPresent()) {
            throw new ProjectNotFoundException();
        }
        return project.get();
    }

    //POST http://localhost:8080/gitminer/projects
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Project create(@Valid @RequestBody Project project) {
        Project _project = new Project();
        _project.setId(project.getId());
        _project.setName(project.getName());
        _project.setWebUrl(project.getWebUrl());
        _project.setCommits(new ArrayList<>(project.getCommits()));
        _project.setIssues(new ArrayList<>(project.getIssues()));
        _project = repository.save(_project);
        return _project;
    }

}
