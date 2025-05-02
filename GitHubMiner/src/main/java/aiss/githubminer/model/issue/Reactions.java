package aiss.githubminer.model.issue;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown=true)
public class Reactions {

    @JsonProperty("+1")
    private Integer plusOne;
    @JsonProperty("-1")
    private Integer minusOne;
    @JsonProperty("laugh")
    private Integer laugh;
    @JsonProperty("hooray")
    private Integer hooray;
    @JsonProperty("confused")
    private Integer confused;
    @JsonProperty("hearth")
    private Integer heart;
    @JsonProperty("rocket")
    private Integer rocket;
    @JsonProperty("eyes")
    private Integer eyes;

    // estos getters, setters y toString han sido generados automáticamente por intellij ya que
    // json2pojo ha decidido no funcionar.

    public Integer getPlusOne() {
        return plusOne;
    }

    public void setPlusOne(Integer plusOne) {
        this.plusOne = plusOne;
    }

    public Integer getMinusOne() {
        return minusOne;
    }

    public void setMinusOne(Integer minusOne) {
        this.minusOne = minusOne;
    }

    public Integer getLaugh() {
        return laugh;
    }

    public void setLaugh(Integer laugh) {
        this.laugh = laugh;
    }

    public Integer getHooray() {
        return hooray;
    }

    public void setHooray(Integer hooray) {
        this.hooray = hooray;
    }

    public Integer getConfused() {
        return confused;
    }

    public void setConfused(Integer confused) {
        this.confused = confused;
    }

    public Integer getHeart() {
        return heart;
    }

    public void setHeart(Integer heart) {
        this.heart = heart;
    }

    public Integer getRocket() {
        return rocket;
    }

    public void setRocket(Integer rocket) {
        this.rocket = rocket;
    }

    public Integer getEyes() {
        return eyes;
    }

    public void setEyes(Integer eyes) {
        this.eyes = eyes;
    }

    @Override
    public String toString() {
        return "Reactions{" +
                "+1=" + plusOne +
                ", -1=" + minusOne +
                ", laugh=" + laugh +
                ", hooray=" + hooray +
                ", confused=" + confused +
                ", heart=" + heart +
                ", rocket=" + rocket +
                ", eyes=" + eyes +
                '}';
    }
}

