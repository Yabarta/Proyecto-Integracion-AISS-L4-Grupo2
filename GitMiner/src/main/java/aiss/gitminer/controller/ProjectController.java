package aiss.gitminer.controller;

import aiss.gitminer.exception.ProjectNotFoundException;
import aiss.gitminer.model.Project;
import aiss.gitminer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/projects")
@Tag(name = "Projects", description = "Operaciones CRUD con los proyectos")
public class ProjectController {

    @Autowired
    ProjectRepository repository;

    //GET http://localhost:8080/gitminer/projects
    @Operation(summary = "Obtener la lista de proyectos")
    @ApiResponse(responseCode = "200", description = "Obtención de los proyectos")
    @GetMapping
    public List<Project> findAll() {
        return repository.findAll();
    }

    //GET http://localhost:8080/gitminer/projects/{id}
    @Operation(summary = "Obtener un proyecto en concreto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proyecto obtenido"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el proyecto")
    })
    @GetMapping("/{id}")
    public Project findOne(@PathVariable String id) throws ProjectNotFoundException {
        Optional<Project> project = repository.findById(id);
        if (!project.isPresent()) {
            throw new ProjectNotFoundException();
        }
        return project.get();
    }

    //POST http://localhost:8080/gitminer/projects
    @Operation(summary = "Creación de un proyecto")
    @ApiResponse(responseCode = "201", description = "Proyecto creado")
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

    //UPDATE http://localhost:8080/gitminer/projects
    @Operation(summary = "Actualización de un proyecto")
    @ApiResponse(responseCode = "204", description = "Proyecto actualizado")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateProject(@Valid @RequestBody Project updatedProject, @PathVariable String id) {
        Optional<Project> p = repository.findById(id);
        Project project = p.get();
        project.setName(updatedProject.getName());
        project.setWebUrl(updatedProject.getWebUrl());
        project.setCommits(updatedProject.getCommits());
        project.setIssues(updatedProject.getIssues());

        repository.save(project);
    }

    //DELETE http://localhost:8080/gitminer/projects
    @Operation(summary = "Eliminación de un proyecto")
    @ApiResponse(responseCode = "204", description = "Proyecto eliminado")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable String id) {
        if(repository.existsById(id)) {
            repository.deleteById(id);
        }
    }

}
