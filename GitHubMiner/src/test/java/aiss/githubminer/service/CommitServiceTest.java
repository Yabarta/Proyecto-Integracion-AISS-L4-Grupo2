package aiss.githubminer.service;

import aiss.githubminer.model.ParsedCommit;
import aiss.githubminer.model.commit.Commit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommitServiceTest {

    @Autowired
    CommitService service;

    @Test
    void getCommits() {
        List<ParsedCommit> commits = service.getCommits("spring-projects","spring-framework",
                null,null,null,50,null);
        assertFalse(commits.isEmpty(), "No commits found");
        System.out.println(commits);
    }
}