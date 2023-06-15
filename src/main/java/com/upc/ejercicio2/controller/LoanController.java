package com.upc.ejercicio2.controller;

import com.upc.ejercicio2.exception.ResourceNotFoundException;
import com.upc.ejercicio2.exception.ValidationException;
import com.upc.ejercicio2.model.Book;
import com.upc.ejercicio2.model.Loan;
import com.upc.ejercicio2.repository.BookRepository;
import com.upc.ejercicio2.repository.LoanRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/library/v1")
public class LoanController {
    private LoanRepository _loanRepository;
    private BookRepository _bookRepository;

    public LoanController(LoanRepository loanRepository, BookRepository bookRepository) {
        _loanRepository = loanRepository;
        _bookRepository = bookRepository;
    }

    //URL: http://localhost:8080/api/library/v1/books/1/loans
    //Method: POST
    @Transactional
    @PostMapping("/books/{id}/loans")
    public ResponseEntity<Loan> createLoan(@PathVariable(value="id") Long bookId, @RequestBody Loan loan) {
        Book book = _bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("No se encontro el libro con el id: " + bookId));
        existsLoanByCodeStudentAndBookAndDevolution(loan, book);
        validateLoan(loan);
        loan.setLoanDate(LocalDate.now());
        loan.setReturnDate(LocalDate.now().plusDays(7));
        loan.setBookLoan(true);

        return new ResponseEntity<Loan>(_loanRepository.save(loan), HttpStatus.CREATED);
    }

    //URL: http://localhost:8080/api/library/v1/loans/filterByCodeStudent
    //Method: GET
    @Transactional(readOnly = true)
    @GetMapping("/loans/filterByCodeStudent")
    public ResponseEntity<List<Loan>> getAllLoansByCodeStudent(@RequestParam(name="codeStudent") String codeStudent){
        return new ResponseEntity<List<Loan>>(_loanRepository.findByCodeStudent(codeStudent), HttpStatus.OK);
    }

    //Validations of the Loan
    private void validateLoan(Loan loan){
        if(loan.getCodeStudent() == null || loan.getCodeStudent().trim().isEmpty()){
            throw new ValidationException("“El código del alumno debe ser obligatorio");
        }
        if(loan.getCodeStudent().length() < 10){
            throw new ValidationException("El código del alumno debe tener 10 caracteres");
        }
    }

    private void existsLoanByCodeStudentAndBookAndDevolution(Loan loan, Book book) {
        if(_loanRepository.existsByCodeStudentAndBookAndBookLoan(loan.getCodeStudent(), book, true)){
            throw new ValidationException("El prestamo del libro " + book.getTitle() + " ya fue prestado por el alumno " + loan.getCodeStudent());
        }
    }
}
