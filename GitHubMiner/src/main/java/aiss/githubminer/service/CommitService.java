package aiss.githubminer.service;

import aiss.githubminer.model.ParsedCommit;
import aiss.githubminer.model.commit.Commit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    public List<ParsedCommit> getCommits(String owner, String repo, Integer page, Integer perPage,
                                         Integer sinceCommits, Integer maxPages) {

        LocalDate sinceDate = LocalDate.now().minusDays(sinceCommits);
        String since = sinceDate.format(DateTimeFormatter.ISO_DATE);

        List<Commit> allCommits = new ArrayList<>();
        int currentPage = (page != null) ? page : 0;

        while (currentPage <= maxPages) {
   
            String url = githubApiUrl + "/" + owner + "/" + repo + "/commits";

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("since", since)
                    .queryParam("page", currentPage)
                    .queryParam("per_page", perPage);

            ResponseEntity<Commit[]> response = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    org.springframework.http.HttpMethod.GET,
                    null,
                    Commit[].class
            );

            List<Commit> commits = Arrays.asList(response.getBody());
            allCommits.addAll(commits);

            if (commits.isEmpty()) {
                break;
            }

            currentPage++;
        }

        List<ParsedCommit> parsedCommits = parseCommit(allCommits);

        return parsedCommits;
    }

    public List<ParsedCommit> parseCommit(List<Commit> commits) {
        List<ParsedCommit> data = new ArrayList<>();
        for (Commit commit : commits) {
            String messageRaw = commit.getCommit().getMessage();
            String[] message = messageRaw.split("\n", 2);
            String title =  message[0];

            ParsedCommit newCommit = new ParsedCommit(
                    commit.getSha(),
                    title,
                    messageRaw,
                    commit.getCommit().getAuthor().getName(),
                    commit.getCommit().getAuthor().getEmail(),
                    commit.getCommit().getAuthor().getDate(),
                    commit.getHtmlUrl()
            );
            data.add(newCommit);
        }
        return data;
    }
}