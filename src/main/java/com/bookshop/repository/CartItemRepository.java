package com.bookshop.repository;

import com.bookshop.entity.Book;
import com.bookshop.entity.CartItem;
import com.bookshop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    List<CartItem> findByUserOrderByIdAsc(User user);

    Optional<CartItem> findByIdAndUser(Long id, User user);

    Optional<CartItem> findByUserAndBook(User user, Book book);

    void deleteByUser(User user);
}
