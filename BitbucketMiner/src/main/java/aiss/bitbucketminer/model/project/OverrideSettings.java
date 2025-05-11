
package aiss.bitbucketminer.model.project;

import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "default_merge_strategy",
    "branching_model"
})
@Generated("jsonschema2pojo")
public class OverrideSettings {

    @JsonProperty("default_merge_strategy")
    private Boolean defaultMergeStrategy;
    @JsonProperty("branching_model")
    private Boolean branchingModel;

    @JsonProperty("default_merge_strategy")
    public Boolean getDefaultMergeStrategy() {
        return defaultMergeStrategy;
    }

    @JsonProperty("default_merge_strategy")
    public void setDefaultMergeStrategy(Boolean defaultMergeStrategy) {
        this.defaultMergeStrategy = defaultMergeStrategy;
    }

    @JsonProperty("branching_model")
    public Boolean getBranchingModel() {
        return branchingModel;
    }

    @JsonProperty("branching_model")
    public void setBranchingModel(Boolean branchingModel) {
        this.branchingModel = branchingModel;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(OverrideSettings.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("defaultMergeStrategy");
        sb.append('=');
        sb.append(((this.defaultMergeStrategy == null)?"<null>":this.defaultMergeStrategy));
        sb.append(',');
        sb.append("branchingModel");
        sb.append('=');
        sb.append(((this.branchingModel == null)?"<null>":this.branchingModel));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
