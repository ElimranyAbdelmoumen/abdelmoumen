package com.bookshop.dto;

import com.bookshop.entity.Category;

public class CategoryResponse {

    private Long id;
    private String name;

    public static CategoryResponse from(Category c) {
        CategoryResponse r = new CategoryResponse();
        r.setId(c.getId());
        r.setName(c.getName());
        return r;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
