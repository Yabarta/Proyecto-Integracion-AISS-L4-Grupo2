package aiss.bitbucketminer.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown=true)
public class ParsedProject {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("web_url")
    private String web_url;
    @JsonProperty("commits")
    private List<ParsedCommit> commits;
    @JsonProperty("issues")
    private List<ParsedIssue> issues;

    public ParsedProject(String id, String name, String web_url) {
        this.id = id;
        this.name = name;
        this.web_url = web_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public List<ParsedCommit> getCommits() {
        return commits;
    }

    public void setCommits(List<ParsedCommit> commits) {
        this.commits = commits;
    }

    public List<ParsedIssue> getIssues() {
        return issues;
    }

    public void setIssues(List<ParsedIssue> issues) {
        this.issues = issues;
    }

    @Override
    public String toString() {
        return "ParsedProject{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", web_url='" + web_url + '\'' +
                ", commits=" + commits +
                ", issues=" + issues +
                '}';
    }
}
