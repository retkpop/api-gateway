package com.tk.api.gateway.service.dto;

public class CategoriesCustomDTO {
    private Long id;
    private String text;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "CategoriesCustomDTO{" +
            "id=" + id +
            ", text='" + text + '\'' +
            '}';
    }
}
