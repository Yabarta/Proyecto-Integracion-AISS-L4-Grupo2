package aiss.githubminer.controller;

import aiss.githubminer.model.comment.Comment;
import aiss.githubminer.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/github")
public class CommentController {

    @Autowired
    private CommentService commentService;
    private RestTemplate restTemplate;
    private final String gitMinerURI = "http://localhost:8080/gitminer";

    @GetMapping("/{owner}/{repoName}/issues/{issue}/comments")
    public List<Comment> getComments(@PathVariable String owner,
                                     @PathVariable String repoName,
                                     @RequestParam(defaultValue = "1") Integer issue, //cambiar esto por un id válido
                                     @RequestParam(defaultValue = "1") Integer page,
                                     @RequestParam(defaultValue = "10") Integer perPage,
                                     @RequestParam(defaultValue = "100") Integer nComments,
                                     @RequestParam(defaultValue = "2") Integer maxPages) {
    return commentService.getComments(owner, repoName, issue, page, perPage, nComments, maxPages);
    }

    @PostMapping("/{owner}/{repoName}/issues/{issue}/comments")
    public List<Comment> sendComments(@PathVariable String owner,
                               @PathVariable String repoName,
                               @RequestParam(defaultValue = "1") Integer issue,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer perPage,
                               @RequestParam(defaultValue = "100") Integer nComments,
                               @RequestParam(defaultValue = "2") Integer maxPages) {
    List<Comment> comments = commentService.getComments(owner, repoName, issue, page, perPage, nComments, maxPages);
    HttpEntity<List<Comment>> request = new HttpEntity<>(comments);
    ResponseEntity<List<Comment>> response =
            restTemplate.exchange(gitMinerURI, HttpMethod.POST, request,  new ParameterizedTypeReference<List<Comment>>() {});
    return response.getBody();
    }


}

