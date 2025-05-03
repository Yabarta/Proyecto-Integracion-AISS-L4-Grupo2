package aiss.githubminer.service;

import aiss.githubminer.model.commit.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommitService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${github.api.url}")
    private String githubApiUrl;

    @Value("${github.token}")
    private String githubToken;

    public List<Commit> getCommits(String owner, String repo, Integer page, Integer perPage, 
    Integer nCommits, Integer sinceCommits, Integer maxPages) {
        // Valores por defecto
        if (sinceCommits == null) {
            sinceCommits = 2; 
        }
        if (maxPages == null) {
            maxPages = 2;
        }
        if (perPage == null) {
            perPage = 10;
        }

        LocalDate sinceDate = LocalDate.now().minusDays(sinceCommits);
        String since = sinceDate.format(DateTimeFormatter.ISO_DATE);

        List<Commit> allCommits = new ArrayList<>();
        int currentPage = (page != null) ? page : 1;

        while (currentPage <= maxPages) {
   
            String url = githubApiUrl + "/" + owner + "/" + repo + "/commits";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + githubToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("since", since)
                    .queryParam("page", currentPage)
                    .queryParam("per_page", perPage);

            ResponseEntity<Commit[]> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    Commit[].class
            );

            List<Commit> commits = Arrays.asList(response.getBody());
            allCommits.addAll(commits);

            if (commits.isEmpty()) {
                break;
            }

            currentPage++;
        }

        if (nCommits != null && nCommits < allCommits.size()) {
            return allCommits.subList(0, nCommits);
        }

        return allCommits;
    }
}