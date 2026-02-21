package com.bookshop.dto;

import com.bookshop.entity.Book;

import java.math.BigDecimal;

public class BookDetailResponse extends BookResponse {

    private String description;

    public static BookDetailResponse from(Book b) {
        BookDetailResponse r = new BookDetailResponse();
        r.setId(b.getId());
        r.setTitle(b.getTitle());
        r.setAuthor(b.getAuthor());
        r.setPrice(b.getPrice());
        r.setStock(b.getStock());
        r.setCategoryName(b.getCategory() != null ? b.getCategory().getName() : null);
        r.setDescription(b.getDescription());
        return r;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
