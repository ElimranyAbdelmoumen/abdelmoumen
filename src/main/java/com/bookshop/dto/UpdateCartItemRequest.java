package com.bookshop.dto;

import jakarta.validation.constraints.Min;

public class UpdateCartItemRequest {

    @Min(1)
    private Integer quantity = 1;

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
