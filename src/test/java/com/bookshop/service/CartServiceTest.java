package com.bookshop.service;

import com.bookshop.dto.CartItemResponse;
import com.bookshop.dto.CartResponse;
import com.bookshop.entity.Book;
import com.bookshop.entity.CartItem;
import com.bookshop.entity.Category;
import com.bookshop.entity.User;
import com.bookshop.exception.ResourceNotFoundException;
import com.bookshop.repository.BookRepository;
import com.bookshop.repository.CartItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CartService cartService;

    private User user;
    private Book book;
    private Category category;

    @BeforeEach
    void setUp() {
        user = new User("user@test.com", "hash", User.Role.USER);
        user.setId(1L);
        category = new Category("Fiction");
        category.setId(1L);
        book = new Book();
        book.setId(10L);
        book.setTitle("Test Book");
        book.setAuthor("Author");
        book.setPrice(new BigDecimal("19.99"));
        book.setStock(5);
        book.setCategory(category);
    }

    @Test
    void getCart_returnsEmptyCartWhenNoItems() {
        when(cartItemRepository.findByUserOrderByIdAsc(user)).thenReturn(Collections.emptyList());

        CartResponse cart = cartService.getCart(user);

        assertThat(cart.getItems()).isEmpty();
        assertThat(cart.getTotal()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void getCart_returnsItemsAndTotal() {
        CartItem item = new CartItem(user, book, 2, book.getPrice());
        item.setId(1L);
        when(cartItemRepository.findByUserOrderByIdAsc(user)).thenReturn(List.of(item));

        CartResponse cart = cartService.getCart(user);

        assertThat(cart.getItems()).hasSize(1);
        assertThat(cart.getItems().get(0).getQuantity()).isEqualTo(2);
        assertThat(cart.getTotal()).isEqualByComparingTo(new BigDecimal("39.98"));
    }

    @Test
    void addItem_createsNewCartItem() {
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));
        when(cartItemRepository.findByUserAndBook(user, book)).thenReturn(Optional.empty());
        CartItem saved = new CartItem(user, book, 1, book.getPrice());
        saved.setId(1L);
        when(cartItemRepository.save(any(CartItem.class))).thenReturn(saved);

        CartItemResponse response = cartService.addItem(user, 10L, 1);

        assertThat(response.getBookId()).isEqualTo(10L);
        assertThat(response.getQuantity()).isEqualTo(1);
        verify(cartItemRepository).save(any(CartItem.class));
    }

    @Test
    void addItem_throwsWhenBookNotFound() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.addItem(user, 999L, 1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void addItem_throwsWhenInsufficientStock() {
        when(bookRepository.findById(10L)).thenReturn(Optional.of(book));

        assertThatThrownBy(() -> cartService.addItem(user, 10L, 10))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Insufficient stock");
    }

    @Test
    void updateItemQuantity_updatesAndReturnsItem() {
        CartItem item = new CartItem(user, book, 1, book.getPrice());
        item.setId(1L);
        when(cartItemRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(item));
        when(cartItemRepository.save(any(CartItem.class))).thenAnswer(inv -> inv.getArgument(0));

        CartItemResponse response = cartService.updateItemQuantity(user, 1L, 3);

        assertThat(response.getQuantity()).isEqualTo(3);
        verify(cartItemRepository).save(item);
    }

    @Test
    void updateItemQuantity_throwsWhenItemNotFound() {
        when(cartItemRepository.findByIdAndUser(999L, user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.updateItemQuantity(user, 999L, 1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void removeItem_deletesItem() {
        CartItem item = new CartItem(user, book, 1, book.getPrice());
        item.setId(1L);
        when(cartItemRepository.findByIdAndUser(1L, user)).thenReturn(Optional.of(item));

        cartService.removeItem(user, 1L);

        verify(cartItemRepository).delete(item);
    }

    @Test
    void removeItem_throwsWhenItemNotFound() {
        when(cartItemRepository.findByIdAndUser(999L, user)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> cartService.removeItem(user, 999L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }
}
