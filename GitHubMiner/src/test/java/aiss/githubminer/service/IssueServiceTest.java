package aiss.githubminer.service;

import aiss.githubminer.model.ParsedIssue;
import aiss.githubminer.model.issue.Issue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IssueServiceTest {

    @Autowired
    IssueService service;

    @Test
    void getIssues() {
        List<ParsedIssue> issues = service.getIssues("spring-projects","spring-framework",
                null,null,null,null);
        assertFalse(issues.isEmpty(),"No issues found");
        System.out.println(issues);
    }
}