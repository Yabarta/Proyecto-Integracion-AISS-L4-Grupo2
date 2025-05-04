package aiss.githubminer.service;

import aiss.githubminer.model.issue.Issue;
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
public class IssueService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${github.api.url}")
    private String githubApiUrl;

    @Value("${github.token}")
    private String githubToken;

    public List<Issue> getIssues(String owner, String repo, Integer page, Integer perPage,
     Integer nIssues, Integer sinceIssues, Integer maxPages) {
        // Valores por defecto
        if (sinceIssues == null) {
            sinceIssues = 20; 
        }
        if (maxPages == null) {
            maxPages = 2;
        }
        if (perPage == null) {
            perPage = 10;
        }

        LocalDate sinceDate = LocalDate.now().minusDays(sinceIssues);
        String since = sinceDate.format(DateTimeFormatter.ISO_DATE);

        List<Issue> allIssues = new ArrayList<>();
        int currentPage = (page != null) ? page : 1;

        while (currentPage <= maxPages) {
       
            String url = githubApiUrl + "/" + owner + "/" + repo + "/issues";

            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + githubToken);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("since", since)
                    .queryParam("page", currentPage)
                    .queryParam("per_page", perPage);

            ResponseEntity<Issue[]> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    org.springframework.http.HttpMethod.GET,
                    entity,
                    Issue[].class
            );

            List<Issue> issues = Arrays.asList(response.getBody());
            allIssues.addAll(issues);

            if (issues.isEmpty()) {
                break;
            }

            currentPage++;
        }

        if (nIssues != null && nIssues < allIssues.size()) {
            return allIssues.subList(0, nIssues);
        }

        return allIssues;

    }
}
