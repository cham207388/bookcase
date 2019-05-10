package com.abc.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.abc.entity.Author;
import com.abc.entity.Book;
import com.abc.response.AuthorResponse;


public interface AuthorService extends UserDetailsService{
	
	public ResponseEntity<AuthorResponse> save(Author author);
	public List<AuthorResponse> findAll();
	public Author findById(int id);
	public Author updateAuthorDeleteBook(int id, Book book);
	public ResponseEntity<Void> deleteById(int id);
	public List<AuthorResponse> findAuthors(int page, int limit);
}