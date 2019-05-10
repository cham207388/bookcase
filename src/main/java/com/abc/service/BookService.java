package com.abc.service;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;

import com.abc.entity.Book;
import com.abc.entity.BookUpdate;
import com.abc.response.BookResponse;

public interface BookService {
	List<Book> findAll();
	Optional<Book> findById(int id);
	Book findByIsbn(String isbn);
	ResponseEntity<BookResponse> findByTitle(String title);
	ResponseEntity<Void> deleteByTitle(String title);
	ResponseEntity<String> deleteAll();
	ResponseEntity<String> updateBook(String title, BookUpdate bookUpdate);
	ResponseEntity<Book> save(Book book);
	void deleteById(int bookId);
	List<BookResponse> findBooks(int page, int limit);

}