package com.abc.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abc.entity.Author;
import com.abc.entity.Book;
import com.abc.exception.BookNotFoundException;
import com.abc.response.AuthorResponse;
import com.abc.service.AuthorService;
import com.abc.service.BookService;


@RestController //http://localhost:8989/
public class AuthorController {
	
	
	private AuthorService authorService;
	private BookService bookService;
	
	@Autowired
	public AuthorController(AuthorService authorService, BookService bookService) {
		super();
		this.authorService = authorService;
		this.bookService = bookService;
	}

	// ==================
	// ===   CREATE   ===
	// ==================	
	@PostMapping(path="/authors",
			produces = {
					MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE},
			consumes = {
					MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<AuthorResponse> save(@Valid @RequestBody Author author){
		return this.authorService.save(author);
	}
	
	// ==================
	// ===    READ    ===
	// ==================	
	@GetMapping(path="/authors/all",
			produces = {
					MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE
			})
	public List<AuthorResponse> getAllAuthors(){
		return this.authorService.findAll();
	}
	
	@GetMapping(path="/authors/id/{id}",
			produces = {
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE})
	public Resource<AuthorResponse> findById(@PathVariable int id) {
		Author author = this.authorService.findById(id);
		
		AuthorResponse authorResponse = new AuthorResponse();
		BeanUtils.copyProperties(author, authorResponse);
		// implementing HATEOAS
		Resource<AuthorResponse> resource = 
				new Resource<AuthorResponse>(authorResponse);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getAllAuthors());
		
		resource.add(linkTo.withRel("all-authors"));
		
		return resource;
	}
	
	// ============================
	// ===   UPDATE  ADD A BOOK ===
	// ============================
	
	@PostMapping(path="/authors/{id}/book",
			produces = {
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE},
			
			consumes = {
					MediaType.APPLICATION_XML_VALUE,
					MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<Author> saveBook(@PathVariable int id, @RequestBody Book book){
		
		Author author = this.authorService.findById(id);
		
		book.setAuthor(author);
		this.bookService.save(book);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(book.getId()).toUri();
		return ResponseEntity.created(location).build();		
	}
	// ================================
	// ===   UPDATE,  DELETE A BOOK ===
	// ================================
	@PutMapping(path="/authors/{id}/delete/{bookId}")
	public ResponseEntity<Void> deleteBookByUpdate(@PathVariable int id, @PathVariable int bookId){
		Author author = this.authorService.findById(id);
		
		List<Book> books = author.getBooks();
		for(Book book : books) {
			if(book.getId() == bookId) {
				books.remove(book);
				this.authorService.save(author);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}
		
		throw new BookNotFoundException("Book is not available!");
	}
	
	// ==================
	// ===   DELETE   ===
	// ==================
	@DeleteMapping(path="/authors/{id}")
	public void deleteAuthor(@PathVariable("id") int id){
		this.authorService.deleteById(id);
	}
	// ======================
	// ===   PAGINATION   ===
	// ======================
	
	@GetMapping(path="/authors/all")
	public List<AuthorResponse> findAuthors(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="limit", defaultValue="5") int limit){
		return this.authorService.findAuthors(page, limit);
	}
}

