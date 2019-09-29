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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abc.entity.Author;
import com.abc.entity.Book;
import com.abc.exception.BookNotFoundException;
import com.abc.model.response.AuthorResponse;
import com.abc.service.AuthorService;
import com.abc.service.BookService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
public class AuthorController {
	
	private AuthorService authorService;
	private BookService bookService;

	@Autowired
	public AuthorController(AuthorService authorService, BookService bookService) {
		super();
		this.authorService = authorService;
		this.bookService = bookService;
	}

	@PostMapping(path = "/author", 
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<AuthorResponse> save(@Valid @RequestBody Author author) {
		log.info("saving an author");
		return this.authorService.save(author);
	}

	@PostMapping(path = "/authors",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE }, 
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<List<AuthorResponse>> saveAll(@RequestBody List<Author> authors) {
		log.info("saving list of authors");
		return this.authorService.saveAll(authors);
	}

	@GetMapping(path = "/authors",
			produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE })
	public List<AuthorResponse> getAllAuthors() {
		log.info("retrieving all authors");
		return this.authorService.findAll();
	}

	@GetMapping(path = "/author/id/{id}",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public Resource<AuthorResponse> findById(@PathVariable int id) {
		log.info("Author with {} is searched", id);
		Author author = this.authorService.findById(id);

		AuthorResponse authorResponse = new AuthorResponse();
		BeanUtils.copyProperties(author, authorResponse);
		Resource<AuthorResponse> resource = new Resource<>(authorResponse);
		ControllerLinkBuilder linkTo = linkTo(methodOn(this.getClass()).getAllAuthors());

		resource.add(linkTo.withRel("authors"));

		return resource;
	}

	@PostMapping(path = "/author/{id}/book",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE },
			consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Author> saveBook(@PathVariable int id, @RequestBody Book book) {
		log.info("adding a book to an author via author id");
		Author author = this.authorService.findById(id);

		book.setAuthor(author);
		this.bookService.save(book);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(book.getId())
				.toUri();
		return ResponseEntity.created(location).build();
	}

	@DeleteMapping(path = "/author/{id}/book/{bookId}")
	public ResponseEntity<Void> deleteBookByUpdate(@PathVariable int id, @PathVariable int bookId) {
		log.info("deleting a book from an author");
		Author author = this.authorService.findById(id);

		List<Book> books = author.getBooks();
		for (Book book : books) {
			if (book.getId() == bookId) {
				books.remove(book);
				this.authorService.save(author);
				return new ResponseEntity<>(HttpStatus.OK);
			}
		}

		throw new BookNotFoundException("Book is not available!");
	}

	@DeleteMapping(path = "/author/{id}")
	public void deleteAuthor(@PathVariable("id") int id) {
		log.info("deleting an author");
		this.authorService.deleteById(id);
	}

	@GetMapping(path = "/pagination",
			produces = { MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_JSON_VALUE })
	public List<AuthorResponse> findAuthors(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "5") int limit) {
		log.info("implementing pagination");
		return this.authorService.findAuthors(page, limit);
	}

	@GetMapping(path = "/response/{id}",
			produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
	public AuthorResponse getResponse(@PathVariable int id){
		return authorService.findResponseByid(id);
	}
}
