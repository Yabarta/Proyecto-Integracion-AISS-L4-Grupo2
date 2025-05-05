package aiss.githubminer.controller;

import aiss.githubminer.model.ParsedComment;
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

    @GetMapping("/{owner}/{repoName}/issues/comments")
    public List<ParsedComment> getAllComments(@PathVariable String owner,
                                              @PathVariable String repoName,
                                              @RequestParam(required=false) Long issue,
                                              @RequestParam(required=false) Integer page,
                                              @RequestParam(required=false) Integer perPage,
                                              @RequestParam(required=false) Integer nComments,
                                              @RequestParam(defaultValue = "2") Integer maxPages) {
    return commentService.getComments(owner, repoName, issue, page, perPage, nComments, maxPages);
    }

    @PostMapping("/{owner}/{repoName}/issues/comments")
    public List<ParsedComment> sendComments(@PathVariable String owner,
                               @PathVariable String repoName,
                               @RequestParam(required=false) Long issue,
                               @RequestParam(required=false) Integer page,
                               @RequestParam(required=false) Integer perPage,
                               @RequestParam(required=false) Integer nComments,
                               @RequestParam(defaultValue = "2") Integer maxPages) {
    List<ParsedComment> comments = commentService.getComments(owner, repoName, issue, page, perPage, nComments, maxPages);
    HttpEntity<List<ParsedComment>> request = new HttpEntity<>(comments);
    ResponseEntity<List<ParsedComment>> response =
            restTemplate.exchange(gitMinerURI, HttpMethod.POST, request,  new ParameterizedTypeReference<List<ParsedComment>>() {});
    return response.getBody();
    }


}

