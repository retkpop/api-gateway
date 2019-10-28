package com.tk.api.gateway.domain.clientResponse;

public class StaticsActions {
    private Number like = 0;
    private Number dislike = 0;
    private Number save = 0;
    private Number share = 0;

    public Number getLike() {
        return like;
    }

    public void setLike(Number like) {
        this.like = like;
    }

    public Number getDislike() {
        return dislike;
    }

    public void setDislike(Number dislike) {
        this.dislike = dislike;
    }

    public Number getSave() {
        return save;
    }

    public void setSave(Number save) {
        this.save = save;
    }

    public Number getShare() {
        return share;
    }

    public void setShare(Number share) {
        this.share = share;
    }

    @Override
    public String toString() {
        return "StaticsActions{" +
            "like=" + like +
            ", dislike=" + dislike +
            ", save=" + save +
            ", share=" + share +
            '}';
    }
}
