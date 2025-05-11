package aiss.gitminer.controller;

import aiss.gitminer.exception.CommentNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.repository.CommentRepository;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/comments")
@Tag(name = "Comments", description = "Operaciones GET con los comments")
public class CommentController {

    @Autowired
    CommentRepository repository;

    //GET http://localhost:8080/gitminer/comments
    @Operation(summary = "Obtener la lista de comments")
    @ApiResponse(responseCode = "200", description = "Obtención de los comments")
    @GetMapping
    public List<Comment> findAll() {
       return repository.findAll();
    }

    //GET http://localhost:8080/gitminer/comments/{id}
    @Operation(summary = "Obtener un comment en concreto")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comment obtenido"),
            @ApiResponse(responseCode = "404", description = "No se ha encontrado el comment")
    })
    @GetMapping("/{id}")
    public Comment findOne(@PathVariable String id) throws CommentNotFoundException {
        Optional<Comment> comment = repository.findById(id);
        if (!comment.isPresent()) {
            throw new CommentNotFoundException();
        }
        return comment.get();
    }

}
