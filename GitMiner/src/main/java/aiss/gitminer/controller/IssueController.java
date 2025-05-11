package aiss.gitminer.controller;

import aiss.gitminer.exception.IssueNotFoundException;
import aiss.gitminer.model.Comment;
import aiss.gitminer.model.Issue;
import aiss.gitminer.repository.IssueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/gitminer/issues")
public class IssueController {

    @Autowired
    IssueRepository repository;

    //GET http://localhost:8080/gitminer/issues
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
    @GetMapping("/{id}")
    public Issue findOne(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if (!issue.isPresent()) {
            throw new IssueNotFoundException();
        }
        return issue.get();
    }

    //GET http://localhost:8080/gitminer/issues/{id}/comments
    @GetMapping("/{id}/comments")
    public List<Comment> findIssueComments(@PathVariable String id) throws IssueNotFoundException {
        Optional<Issue> issue = repository.findById(id);
        if (!issue.isPresent()) {
            throw new IssueNotFoundException();
        }
        return issue.get().getComments();
    }

}
