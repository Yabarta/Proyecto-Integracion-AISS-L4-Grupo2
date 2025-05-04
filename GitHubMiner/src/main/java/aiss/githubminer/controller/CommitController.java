package aiss.githubminer.controller;

import aiss.githubminer.service.CommitService;
import aiss.githubminer.model.commit.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/githubminer")

public class CommitController {

    @Autowired
    private CommitService commitService;
    @Autowired
    private RestTemplate restTemplate;
    private final String gitMinerURI = "http://localhost:8080/gitminer"

    @GetMapping("/{owner}/{repoName}")
    public List<Commit> getCommits(@PathVariable String owner,
                               @PathVariable String repoName,
                               @RequestParam int page,
                               @RequestParam int perPage,
                               @RequestParam int nCommits,
                               @RequestParam(defaultValue = "2") int sinceCommits,
                               @RequestParam(defaultValue = "2") int maxPages) {
    return commitService.getCommits(owner, repoName, page, perPage, nCommits, sinceCommits, maxPages);
    }

    @PostMapping("/{owner}/{repoName}")
    public List<Commit> sendCommits(@PathVariable String owner,
                                @PathVariable String repoName,
                                @RequestParam int page,
                                @RequestParam int perPage,
                                @RequestParam int nCommits,
                                @RequestParam(defaultValue = "2") int sinceCommits,
                                @RequestParam(defaultValue = "2") int maxPages) {
    List<Commit> commits = commitService.getCommits(owner, repoName, page, perPage, nCommits, sinceCommits, maxPages); 
    HttpEntity<List<Commit>> request = new HttpEntity<>(commits);
    ResponseEntity<List<Commit>> response = 
            restTemplate.exchange(gitMinerURI, HttpMethod.POST, request, List.class);
    return response.getBody();
    }
}

