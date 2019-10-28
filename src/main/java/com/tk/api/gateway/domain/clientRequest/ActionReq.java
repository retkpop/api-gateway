package com.tk.api.gateway.domain.clientRequest;

import com.tk.api.gateway.domain.enumeration.Action;

public class ActionReq {

    private Action action;
    private Long post_id;

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public Long getPost_id() {
        return post_id;
    }

    public void setPost_id(Long post_id) {
        this.post_id = post_id;
    }

    @Override
    public String toString() {
        return "ActionReq{" +
            "action=" + action +
            ", post_id=" + post_id +
            '}';
    }
}
