package aiss.githubminer.controller;

import aiss.githubminer.service.ProjectService;
import aiss.githubminer.model.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/githubminer")

public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private RestTemplate restTemplate;
    private final String gitMinerURI = "http://localhost:8080/gitminer"

    @GetMapping("/{owner}/{repoName}")
    public List<Comment> getComments(@PathVariable String owner,
                               @PathVariable String repoName,
                               @RequestParam int page,
                               @RequestParam int Integer perPage,
                               @RequestParam int Integer nComments,
                               @RequestParam(defaultValue = "2") Integer sinceComments,
                               @RequestParam(defaultValue = "2") Integer maxPages) {
    return commentService.getComments(owner, repoName, page, perPage, nComments, sinceComments, maxPages);
    }


}

