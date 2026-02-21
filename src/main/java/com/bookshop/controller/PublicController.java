package com.bookshop.controller;

import com.bookshop.dto.BookDetailResponse;
import com.bookshop.dto.BookResponse;
import com.bookshop.dto.CategoryResponse;
import com.bookshop.entity.Category;
import com.bookshop.repository.CategoryRepository;
import com.bookshop.service.BookService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/public")
public class PublicController {

    private final CategoryRepository categoryRepository;
    private final BookService bookService;

    public PublicController(CategoryRepository categoryRepository, BookService bookService) {
        this.categoryRepository = categoryRepository;
        this.bookService = bookService;
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> listCategories() {
        List<Category> categories = categoryRepository.findAllByOrderByNameAsc();
        List<CategoryResponse> body = categories.stream().map(CategoryResponse::from).collect(Collectors.toList());
        return ResponseEntity.ok(body);
    }

    @GetMapping("/books")
    public ResponseEntity<Page<BookResponse>> listBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(bookService.findAllAsResponse(pageable));
    }

    @GetMapping("/books/{id}")
    public ResponseEntity<BookDetailResponse> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.findByIdAsDetailResponse(id));
    }
}
