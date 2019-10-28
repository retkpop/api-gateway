package com.tk.api.gateway.domain.clientResponse;

public class SubscribeRes {
    private Long code;
    private String status;
    private String message;

    public Long getCode() {
        return code;
    }

    public void setCode(Long code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "SubscribeRes{" +
            "code=" + code +
            ", status=" + status +
            ", message='" + message + '\'' +
            '}';
    }
}
