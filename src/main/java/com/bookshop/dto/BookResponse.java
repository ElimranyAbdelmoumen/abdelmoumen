package com.bookshop.dto;

import com.bookshop.entity.Book;

import java.math.BigDecimal;

public class BookResponse {

    private Long id;
    private String title;
    private String author;
    private BigDecimal price;
    private Integer stock;
    private String categoryName;

    public static BookResponse from(Book b) {
        BookResponse r = new BookResponse();
        r.setId(b.getId());
        r.setTitle(b.getTitle());
        r.setAuthor(b.getAuthor());
        r.setPrice(b.getPrice());
        r.setStock(b.getStock());
        r.setCategoryName(b.getCategory() != null ? b.getCategory().getName() : null);
        return r;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
