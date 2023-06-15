package com.upc.ejercicio2.service.implement;

import com.upc.ejercicio2.model.Book;
import com.upc.ejercicio2.repository.BookRepository;
import com.upc.ejercicio2.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookServiceImpl implements BookService {
    @Autowired
    private BookRepository repository;
    @Override
    public Book createBook(Book book) {
        return repository.save(book);
    }
}
