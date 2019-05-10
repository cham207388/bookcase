package com.abc.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.abc.entity.Book;
import com.abc.entity.BookUpdate;
import com.abc.repo.BookRepository;
import com.abc.response.BookResponse;
import com.abc.service.BookService;

@Service
public class BookServiceImpl implements BookService{
	
	private BookRepository bookRepository;
	
	@Autowired
	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}
	
	@Override
	public List<Book> findAll(){
		List<Book> books = new ArrayList<Book>();
		this.bookRepository.findAll().forEach(books::add);
		
		return books;
	}
	@Override
	public Optional<Book> findById(int id){
		return this.bookRepository.findById(id);
	}
	@Override
	public Book findByIsbn(String isbn) {
		return this.bookRepository.findByIsbn(isbn);
	}
	
	@Override
	public ResponseEntity<BookResponse> findByTitle(String title) {
		Book book = this.bookRepository.findByTitle(title);
		BookResponse response = new BookResponse();
		BeanUtils.copyProperties(book, response);
		response.setFullName(book.getAuthor().getFirstName()+" "+book.getAuthor().getLastName());
		
		return new ResponseEntity<BookResponse>(response, HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<Void> deleteByTitle(String title){
		this.bookRepository.deleteByTitle(title);
		
		return ResponseEntity.noContent().build();
	}
	
	@Override
	public ResponseEntity<String> deleteAll(){
		this.bookRepository.deleteAll();
		
		return new ResponseEntity<String>("All books are deleted", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<String> updateBook(String title, BookUpdate bookUpdate){
		
		Book bookData = this.bookRepository.findByTitle(title); //get a book by its title
		
		if(bookData != null && bookUpdate.getTitle().equals(title)) {
			String ed = bookData.getEdition();
			bookData.setEdition(bookUpdate.getEdition());
			bookData.setPrice(bookUpdate.getPrice());
			
			this.bookRepository.save(bookData);
			
			return new ResponseEntity<String>("edition update from "+ed+" to "+
					bookUpdate.getEdition(), HttpStatus.CREATED);
		}
		
		return new ResponseEntity<>("Book not updated", HttpStatus.OK);
	}
	
	@Override
	public ResponseEntity<Book> save(Book book) {
		Book b = this.bookRepository.save(book);
		
		return new ResponseEntity<Book>(b, HttpStatus.CREATED);
	}
	@Override
	public void deleteById(int bookId) {
		this.bookRepository.deleteById(bookId);
	}

	@Override
	public List<BookResponse> findBooks(int page, int limit) {
		if(page > 0) page -=1;
		List<BookResponse> bookResponses = new ArrayList<>();
		BookResponse bookResponse = null;
		
		Pageable pageable = PageRequest.of(page, limit);
		Page<Book> bookPage = bookRepository.findAll(pageable);
		List<Book> storedBooks = bookPage.getContent();
		for(Book book : storedBooks) {
			bookResponse = new BookResponse();
			BeanUtils.copyProperties(book, bookResponse);
			bookResponse.setFullName(book.getAuthor().getFirstName()+
					" "+book.getAuthor().getLastName());
			bookResponses.add(bookResponse);
			bookResponse = null;
		}
		return bookResponses;
	}
}
