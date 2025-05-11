
package aiss.bitbucketminer.model.commit;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "values",
    "pagelen"
})
public class CommitList {

    @JsonProperty("values")
    private List<Commit> values;
    @JsonProperty("pagelen")
    private Integer pagelen;

    @JsonProperty("values")
    public List<Commit> getValues() {
        return values;
    }

    @JsonProperty("values")
    public void setValues(List<Commit> values) {
        this.values = values;
    }

    @JsonProperty("pagelen")
    public Integer getPagelen() {
        return pagelen;
    }

    @JsonProperty("pagelen")
    public void setPagelen(Integer pagelen) {
        this.pagelen = pagelen;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CommitList.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("values");
        sb.append('=');
        sb.append(((this.values == null)?"<null>":this.values));
        sb.append(',');
        sb.append("pagelen");
        sb.append('=');
        sb.append(((this.pagelen == null)?"<null>":this.pagelen));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

}
