package aiss.githubminer.service;

import aiss.githubminer.model.project.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Arrays;
import java.util.List;

@Service
public class ProjectService {

    @Autowired
    RestTemplate restTemplate;

    public Project getProjectData(String owner, String repo) {
        String url = "http://localhost:8082/github/" + owner + "/" + repo;

        // Construir la URL con parámetros opcionales
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("sinceCommits", 5) // Parámetro de ejemplo
                .queryParam("sinceIssues", 30) // Parámetro de ejemplo
                .queryParam("maxPages", 2);    // Parámetro de ejemplo

        // Hacer la solicitud GET
        ResponseEntity<Project> response = restTemplate.getForEntity(uriBuilder.toUriString(), Project.class);

        return response.getBody();
    }
}
