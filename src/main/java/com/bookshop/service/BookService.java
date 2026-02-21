package com.bookshop.service;

import com.bookshop.dto.BookCreateRequest;
import com.bookshop.dto.BookDetailResponse;
import com.bookshop.dto.BookResponse;
import com.bookshop.entity.Book;
import com.bookshop.entity.Category;
import com.bookshop.exception.ResourceNotFoundException;
import com.bookshop.repository.BookRepository;
import com.bookshop.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;

    public BookService(BookRepository bookRepository, CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
    }

    public Page<Book> findAll(Pageable pageable) {
        return bookRepository.findAllByOrderByIdAsc(pageable);
    }

    @Transactional(readOnly = true)
    public Page<BookResponse> findAllAsResponse(Pageable pageable) {
        return bookRepository.findAllByOrderByIdAsc(pageable).map(BookResponse::from);
    }

    public Book findById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found: " + id));
    }

    @Transactional(readOnly = true)
    public BookDetailResponse findByIdAsDetailResponse(Long id) {
        return BookDetailResponse.from(findById(id));
    }

    public Book create(BookCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found: " + request.getCategoryId()));
        Book book = new Book();
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setPrice(request.getPrice());
        book.setStock(request.getStock() != null ? request.getStock() : 0);
        book.setDescription(request.getDescription());
        book.setCategory(category);
        return bookRepository.save(book);
    }

    public void deleteById(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found: " + id);
        }
        bookRepository.deleteById(id);
    }
}
