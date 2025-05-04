package aiss.githubminer.service;

import aiss.githubminer.model.comment.Comment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CommentServiceTest {

    @Autowired
    CommentService service;

    @Test
    void getComments() {
        List<Comment> comments = service.getComments("spring-projects","spring-framework",
                null,null,null,null,null);
        assertFalse(comments.isEmpty(),"No comments found");
        System.out.println(comments);
    }
}