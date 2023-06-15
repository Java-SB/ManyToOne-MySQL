package com.upc.ejercicio2.repository;

import com.upc.ejercicio2.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByEditorial(String editorial);
    Boolean existsByTitleAndEditorial(String title, String editorial);
}
