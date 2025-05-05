package aiss.githubminer.service;

import aiss.githubminer.model.ParsedProject;
import aiss.githubminer.model.project.Project;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ProjectServiceTest {

    @Autowired
    ProjectService service;

    @Test
    @DisplayName("Get project data")
    void getProjectData() {
        ParsedProject project = service.getProjectData("spring-projects","spring-framework",null,
                null,null,null,null,null,null);
        assertNotNull(project,"No project found");
        System.out.println(project);
    }
}