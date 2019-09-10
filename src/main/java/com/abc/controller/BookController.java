package com.abc.controller;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.abc.entity.Author;
import com.abc.entity.Book;
import com.abc.response.BookResponse;
import com.abc.service.AuthorService;
import com.abc.service.BookService;

@RestController
public class BookController {
	
	private BookService bookService;
	private AuthorService authorService;	
	private AuthorController authorController;
	
	@Autowired
	public BookController(BookService bookService, AuthorService authorService, AuthorController authorController) {
		super();
		this.bookService = bookService;
		this.authorService = authorService;
		this.authorController = authorController;
	}

	@GetMapping(path="/authors/books",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<BookResponse> findAll(){
		List<Book> books = this.bookService.findAll();
		List<BookResponse> response = new ArrayList<>();
		BookResponse res = null;
		for(Book book : books) {
			res = new BookResponse();
			BeanUtils.copyProperties(book, res);
			res.setFullName(book.getAuthor().getFirstName()+" "+book.getAuthor().getLastName());
			response.add(res);
		}
		
		return response;
	}
 
	@GetMapping(path="/author/{id}/books",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public List<BookResponse> findByAuthor(@PathVariable int id) {
		Author author = this.authorService.findById(id);
		List<Book> books = author.getBooks();
		List<BookResponse> response = new ArrayList<>();
		BookResponse res = null;
		for(Book book : books) {
			res = new BookResponse();
			BeanUtils.copyProperties(book, res);
			res.setFullName(book.getAuthor().getFirstName()+" "+book.getAuthor().getLastName());
			response.add(res);
		}
		
		return response;
	}
	
	@GetMapping(path="/book/isbn/{isbn}",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public Resource<BookResponse> findByIsbn(@PathVariable String isbn) {
		Book book = this.bookService.findByIsbn(isbn);
		BookResponse response = new BookResponse();
		BeanUtils.copyProperties(book, response);
		response.setFullName(book.getAuthor().getFirstName()+" "+book.getAuthor().getLastName());
		int authorId = book.getAuthor().getId();
		Resource<BookResponse> resource = new Resource<BookResponse>(response);
		ControllerLinkBuilder linkToAuthors = linkTo(methodOn(this.getClass()).findAll());
		ControllerLinkBuilder linkToAllBooksByAuthor = linkTo(methodOn(this.getClass()).findByAuthor(authorId));
		ControllerLinkBuilder linkToAuthorDetail = linkTo(methodOn(
				this.authorController
				.getClass())
				.findById(authorId));
		resource.add(linkToAuthors.withRel("all-books"));
		resource.add(linkToAuthorDetail.withRel("author-detail"));
		resource.add(linkToAllBooksByAuthor.withRel("all-books-by-author"));
		
		return resource;
	}
	
	@GetMapping(path="/book/title/{title}",
			produces = {MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<BookResponse> findByTitle(@PathVariable String title) {
		return this.bookService.findByTitle(title);
	}
	
	@GetMapping(path="/books/all")
	public List<BookResponse> findAuthors(
			@RequestParam(value="page", defaultValue="0") int page,
			@RequestParam(value="limit", defaultValue="5") int limit){
		return this.bookService.findBooks(page, limit);
	}
}
