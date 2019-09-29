package com.abc.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.abc.entity.Author;
import com.abc.entity.Book;
import com.abc.model.response.AuthorResponse;


public interface AuthorService extends UserDetailsService{
	
	ResponseEntity<AuthorResponse> save(Author author);
	ResponseEntity<List<AuthorResponse>> saveAll(List<Author> author);
	List<AuthorResponse> findAll();
	Author findById(int id);
	Author updateAuthorDeleteBook(int id, Book book);
	ResponseEntity<Void> deleteById(int id);
	List<AuthorResponse> findAuthors(int page, int limit);
	AuthorResponse findResponseByid(int id);


    Author findByEmail(String email);
}