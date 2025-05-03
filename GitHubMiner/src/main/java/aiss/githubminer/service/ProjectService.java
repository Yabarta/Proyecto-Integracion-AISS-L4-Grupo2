package aiss.githubminer.service;

import aiss.githubminer.model.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;

    @Value("${github.api.url}")
    private String githubApiUrl;

    @Value("${github.token}")
    private String githubToken;

    public Project getProjectData(String owner, String repo) {
    
        String url = githubApiUrl + "/" + owner + "/" + repo;

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + githubToken);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url);

        ResponseEntity<Project> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                org.springframework.http.HttpMethod.GET,
                entity,
                Project.class
        );

        return response.getBody();
    }
}
