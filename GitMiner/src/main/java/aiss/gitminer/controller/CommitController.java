package aiss.gitminer.controller;

import aiss.gitminer.exception.CommitNotFoundException;
import aiss.gitminer.model.Commit;
import aiss.gitminer.repository.CommitRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/commits")
@Tag(name = "Commits", description = "Operaciones GET con los commits")
public class CommitController {

    @Autowired
    CommitRepository repository;

    //GET http://localhost:8080/gitminer/commits
    @Operation(summary = "Obtener la lista de commits")
    @ApiResponse(responseCode = "200", description = "Obtención de los commits")
    @GetMapping
    public List<Commit> findAll() {
        return repository.findAll();
    }

    //GET http://localhost:8080/gitminer/commits/{id}
    @Operation(summary = "Obtener un commit en concreto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Commit obtenido"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el commit")
    })
    @GetMapping("/{id}")
    public Commit findOne(@PathVariable String id) throws CommitNotFoundException {
        Optional<Commit> commit = repository.findById(id);
        if (!commit.isPresent()) {
            throw new CommitNotFoundException();
        }
        return commit.get();
    }

}
