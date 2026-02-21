package com.bookshop.service;

import com.bookshop.dto.CartItemResponse;
import com.bookshop.dto.CartResponse;
import com.bookshop.entity.Book;
import com.bookshop.entity.CartItem;
import com.bookshop.entity.User;
import com.bookshop.exception.ResourceNotFoundException;
import com.bookshop.repository.BookRepository;
import com.bookshop.repository.CartItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final BookRepository bookRepository;

    public CartService(CartItemRepository cartItemRepository, BookRepository bookRepository) {
        this.cartItemRepository = cartItemRepository;
        this.bookRepository = bookRepository;
    }

    public CartResponse getCart(User user) {
        List<CartItem> items = cartItemRepository.findByUserOrderByIdAsc(user);
        List<CartItemResponse> dtos = items.stream().map(CartItemResponse::from).collect(Collectors.toList());
        BigDecimal total = items.stream().map(CartItem::getSubTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        return new CartResponse(dtos, total);
    }

    @Transactional
    public CartItemResponse addItem(User user, Long bookId, Integer quantity) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + bookId));
        if (book.getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + book.getStock());
        }
        var existing = cartItemRepository.findByUserAndBook(user, book);
        CartItem item;
        if (existing.isPresent()) {
            item = existing.get();
            int newQty = item.getQuantity() + quantity;
            if (book.getStock() < newQty) {
                throw new IllegalArgumentException("Insufficient stock. Available: " + book.getStock());
            }
            item.setQuantity(newQty);
        } else {
            item = new CartItem(user, book, quantity, book.getPrice());
        }
        item = cartItemRepository.save(item);
        return CartItemResponse.from(item);
    }

    @Transactional
    public CartItemResponse updateItemQuantity(User user, Long itemId, Integer quantity) {
        CartItem item = cartItemRepository.findByIdAndUser(itemId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + itemId));
        if (item.getBook().getStock() < quantity) {
            throw new IllegalArgumentException("Insufficient stock. Available: " + item.getBook().getStock());
        }
        item.setQuantity(quantity);
        item = cartItemRepository.save(item);
        return CartItemResponse.from(item);
    }

    @Transactional
    public void removeItem(User user, Long itemId) {
        CartItem item = cartItemRepository.findByIdAndUser(itemId, user)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found: " + itemId));
        cartItemRepository.delete(item);
    }
}
