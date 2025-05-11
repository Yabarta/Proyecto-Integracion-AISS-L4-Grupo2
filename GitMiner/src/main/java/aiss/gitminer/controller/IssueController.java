package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/issues")
@Tag(name = "Issues", description = "Operaciones GET con los issues")
public class IssueController {

    @Autowired
    IssueRepository repository;

    //GET http://localhost:8080/gitminer/issues
    @Operation(summary = "Obtener la lista de issues")
    @ApiResponse(responseCode = "200", description = "Obtención de los issues")
    @GetMapping
    public List<Issue> findAll(@RequestParam(required=false) String authorId,
                               @RequestParam(required=false) String state) {
        List<Issue> issues;
        if (authorId != null) {
            issues = repository.findByAuthorId(authorId);
        } else if (state != null) {
            issues = repository.findByState(state);
        } else {
            issues = repository.findAll();
        }
        return issues;
    }

    //GET http://localhost:8080/gitminer/issues/{id}
    @Operation(summary = "Obtener un issue en concreto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Issue obtenido"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el proyecto")
    })
    @GetMapping("/{id}")
    public Issue findOne(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if (!issue.isPresent()) {
            throw new IssueNotFoundException();
        }
        return issue.get();
    }

    //GET http://localhost:8080/gitminer/issues/{id}/comments
    @Operation(summary = "Obtener la lista de comments de un issue")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de comments obtenida"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el issue")
    })
    @GetMapping("/{id}/comments")
    public List<Comment> findIssueComments(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if (!issue.isPresent()) {
            throw new IssueNotFoundException();
        }
        return issue.get().getComments();
    }

}
