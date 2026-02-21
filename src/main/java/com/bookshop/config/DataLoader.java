package com.bookshop.config;

import com.bookshop.entity.Book;
import com.bookshop.entity.Category;
import com.bookshop.entity.User;
import com.bookshop.repository.BookRepository;
import com.bookshop.repository.CategoryRepository;
import com.bookshop.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataLoader {

    @Bean
    CommandLineRunner loadData(UserRepository userRepository,
                               CategoryRepository categoryRepository,
                               BookRepository bookRepository,
                               PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() > 0) {
                return;
            }
            // Users (mots de passe hashés - ne jamais committer les clairs)
            User admin = new User("admin@bookshop.com", passwordEncoder.encode("admin123"), User.Role.ADMIN);
            User user = new User("user@bookshop.com", passwordEncoder.encode("user123"), User.Role.USER);
            userRepository.saveAll(List.of(admin, user));

            // Catégories
            Category roman = categoryRepository.save(new Category("Roman"));
            Category bd = categoryRepository.save(new Category("Bande dessinée"));
            Category informatique = categoryRepository.save(new Category("Informatique"));

            // Livres
            Book b1 = new Book();
            b1.setTitle("Le Petit Prince");
            b1.setAuthor("Antoine de Saint-Exupéry");
            b1.setPrice(new BigDecimal("8.50"));
            b1.setStock(100);
            b1.setDescription("Conte poétique et philosophique.");
            b1.setCategory(roman);

            Book b2 = new Book();
            b2.setTitle("Astérix chez les Bretons");
            b2.setAuthor("Goscinny, Uderzo");
            b2.setPrice(new BigDecimal("12.00"));
            b2.setStock(50);
            b2.setDescription("Aventure d'Astérix et Obélix.");
            b2.setCategory(bd);

            Book b3 = new Book();
            b3.setTitle("Spring Boot in Action");
            b3.setAuthor("Craig Walls");
            b3.setPrice(new BigDecimal("45.00"));
            b3.setStock(20);
            b3.setDescription("Guide pratique Spring Boot.");
            b3.setCategory(informatique);

            bookRepository.saveAll(List.of(b1, b2, b3));
        };
    }
}
