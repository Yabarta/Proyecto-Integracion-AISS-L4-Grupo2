package aiss.bitbucketminer.service;

import aiss.bitbucketminer.model.commit.Commit;
import aiss.bitbucketminer.model.commit.CommitList;
import aiss.bitbucketminer.model.ParsedCommit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommitService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${bitbucket.api.url}")
    private String bitbucketApiUrl;

    public List<ParsedCommit> getCommits(String workspace, String repo_slug,
                                         Integer nCommits, Integer maxPages) {

        String uri = bitbucketApiUrl + "/" + workspace + "/" + repo_slug + "/commits";

        ResponseEntity<CommitList> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                null,
                CommitList.class
        );

        List<Commit> pagedCommits = new ArrayList<>(response.getBody().getValues());
        pagedCommits = pagedCommits.stream().limit(maxPages * nCommits).toList();
        response.getBody().setValues(pagedCommits);

        List<ParsedCommit> parsedCommits = parseCommits(response.getBody());

        return parsedCommits;
    }

    public List<ParsedCommit> parseCommits(CommitList commits) {
        List<ParsedCommit> data = new ArrayList<>();
        for (Commit commit : commits.getValues()) {
            String message = commit.getMessage();
            String title = message.split("\n", 2)[0];
            String[] authorRaw = commit.getAuthor().getRaw().split("<");
            String authorName = authorRaw[0];
            String authorEmail = authorRaw[1].replace(">", "");

            ParsedCommit newCommit = new ParsedCommit(
                    commit.getHash(),
                    title,
                    message,
                    authorName,
                    authorEmail,
                    commit.getDate(),
                    commit.getLinks().getHtml().getHref()
            );
            data.add(newCommit);
        }
        return data;
    }
}
