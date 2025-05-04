package aiss.githubminer.controller;

import aiss.githubminer.model.commit.Commit;
import aiss.githubminer.service.CommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/github")
public class CommitController {

    @Autowired
    private CommitService commitService;
    @Autowired
    private RestTemplate restTemplate;
    private final String gitMinerURI = "http://localhost:8080/gitminer";

    @GetMapping("/{owner}/{repoName}/commits")
    public List<Commit> getCommits(@PathVariable String owner,
                                   @PathVariable String repoName,
                                   @RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer perPage,
                                   @RequestParam(defaultValue = "100") Integer nCommits,
                                   @RequestParam(defaultValue = "2") Integer sinceCommits,
                                   @RequestParam(defaultValue = "2") Integer maxPages) {
        if (page <= 0 || perPage <= 0 || nCommits <= 0) {
            throw new IllegalArgumentException("Los parámetros page, perPage y nCommits deben ser mayores que 0.");
        }
        return commitService.getCommits(owner, repoName, page, perPage, nCommits, sinceCommits, maxPages);
    }

    @PostMapping("/{owner}/{repoName}/commits")
    public List<Commit> sendCommits(@PathVariable String owner,
                                @PathVariable String repoName,
                                @RequestParam(defaultValue = "1") Integer page,
                                @RequestParam(defaultValue = "10") Integer perPage,
                                @RequestParam(defaultValue = "100") Integer nCommits,
                                @RequestParam(defaultValue = "2") Integer sinceCommits,
                                @RequestParam(defaultValue = "2") Integer maxPages)  {
    List<Commit> commits = commitService.getCommits(owner, repoName, page, perPage, nCommits, sinceCommits, maxPages); 
    HttpEntity<List<Commit>> request = new HttpEntity<>(commits);
    ResponseEntity<List<Commit>> response = 
            restTemplate.exchange(gitMinerURI, HttpMethod.POST, request, List.class);
    return response.getBody();
    }


}

