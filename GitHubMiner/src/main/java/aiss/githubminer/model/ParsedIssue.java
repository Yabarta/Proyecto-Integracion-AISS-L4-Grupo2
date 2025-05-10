package aiss.githubminer.model;

import aiss.githubminer.model.comment.Comment;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ParsedIssue {

    @JsonProperty("id")
    private String id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("description")
    private String description;
    @JsonProperty("state")
    private String state;
    @JsonProperty("created_at")
    private String created_at;
    @JsonProperty("updated_at")
    private String updated_at;
    @JsonProperty("closed_at")
    private String closed_at;
    @JsonProperty("labels")
    private List<String> labels;
    @JsonProperty("votes")
    private Integer votes;
    @JsonProperty("comments")
    private List<ParsedComment> comments;
    @JsonProperty("author")
    private ParsedUser author;
    @JsonProperty("assignee")
    private ParsedUser assignee;

    public ParsedIssue(String id, String title, String description, String state,
                       String created_at, String updated_at, String closed_at, List<String> labels,
                       ParsedUser author, ParsedUser assignee, Integer votes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.state = state;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.closed_at = closed_at;
        this.labels = labels;
        this.author = author;
        this.assignee = assignee;
        this.votes = votes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getClosed_at() {
        return closed_at;
    }

    public void setClosed_at(String closed_at) {
        this.closed_at = closed_at;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public ParsedUser getAuthor() {
        return author;
    }

    public void setAuthor(ParsedUser author) {
        this.author = author;
    }

    public ParsedUser getAssignee() {
        return assignee;
    }

    public void setAssignee(ParsedUser assignee) {
        this.assignee = assignee;
    }

    public List<ParsedComment> getComments() {
        return comments;
    }

    public void setComments(List<ParsedComment> comments) {
        this.comments = comments;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    @Override
    public String toString() {
        return "ParsedIssue{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author=" + author + '\'' +
                ", assignee=" + assignee + '\'' +
                ", state='" + state + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", closed_at='" + closed_at + '\'' +
                ", labels=" + labels +
                ", comments=" + comments +
                ", votes=" + votes +
                '}';
    }
}
