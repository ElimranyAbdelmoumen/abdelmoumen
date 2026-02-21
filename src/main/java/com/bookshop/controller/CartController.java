package com.bookshop.controller;

import com.bookshop.dto.AddCartItemRequest;
import com.bookshop.dto.CartItemResponse;
import com.bookshop.dto.CartResponse;
import com.bookshop.dto.UpdateCartItemRequest;
import com.bookshop.entity.User;
import com.bookshop.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@AuthenticationPrincipal User user) {
        CartResponse cart = cartService.getCart(user);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<CartItemResponse> addItem(@AuthenticationPrincipal User user,
                                                    @Valid @RequestBody AddCartItemRequest request) {
        CartItemResponse item = cartService.addItem(user, request.getBookId(), request.getQuantity());
        return ResponseEntity.ok(item);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartItemResponse> updateItem(@AuthenticationPrincipal User user,
                                                        @PathVariable Long itemId,
                                                        @Valid @RequestBody UpdateCartItemRequest request) {
        CartItemResponse item = cartService.updateItemQuantity(user, itemId, request.getQuantity());
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<Void> removeItem(@AuthenticationPrincipal User user, @PathVariable Long itemId) {
        cartService.removeItem(user, itemId);
        return ResponseEntity.noContent().build();
    }
}
