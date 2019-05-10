package com.abc.service.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abc.entity.Author;
import com.abc.entity.Book;
import com.abc.exception.AuthorNotFoundException;
import com.abc.repo.AuthorRepository;
import com.abc.response.AuthorResponse;
import com.abc.service.AuthorService;

@Service
public class AuthorServiceImpl implements AuthorService{
	
	
	private AuthorRepository authorRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	
	@Autowired
	public AuthorServiceImpl(AuthorRepository authorRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
		super();
		this.authorRepository = authorRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}
	public AuthorServiceImpl() {
	}

	// ==================
	// ===   CREATE   ===
	// ==================
	
	@Override
	public ResponseEntity<AuthorResponse> save(Author author){
		author.setPassword(bCryptPasswordEncoder.encode(author.getPassword()));
		Author storedAuthor = this.authorRepository.save(author);
		
		URI location = ServletUriComponentsBuilder
				.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(storedAuthor.getId()).toUri();

		return ResponseEntity.created(location).build();
				
	}
	
	// ==================
	// ===    READ    ===
	// ==================
	
	@Override
	public List<AuthorResponse> findAll(){
		List<Author> storedAuthors = new ArrayList<Author>();
		this.authorRepository.findAll().forEach(storedAuthors::add);
		List<AuthorResponse> authorResponses = new ArrayList<>();
		AuthorResponse authorResponse = null;
		for(Author author : storedAuthors) {
			authorResponse = new AuthorResponse();
			BeanUtils.copyProperties(author, authorResponse);
			authorResponses.add(authorResponse);
		}
		
		return authorResponses;
		
	}
	
	@Override
	public Author findById(int id) {
		Optional<Author> authorOptional = this.authorRepository.findById(id);
		if(!authorOptional.isPresent()) {
			throw new AuthorNotFoundException("No author with id= "+id);
		}
		return authorOptional.get();
	}
		
	@Override
	public Author updateAuthorDeleteBook(int id, Book book) {
		Optional<Author> authorData = this.authorRepository.findById(id);
		if (authorData.isPresent()) {
			Author author = authorData.get();
			if (author.getBooks().contains(book)) {
				author.removeBook(book);
			}
			return this.authorRepository.save(author);
		}

		throw new AuthorNotFoundException("Update not completed");	
	}
	// ==================
	// ===   DELETE   ===
	// ==================
	@Override
	public ResponseEntity<Void> deleteById(int id){
		this.authorRepository.deleteById(id);
		
		return ResponseEntity.noContent().build();
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Author author = this.authorRepository.findByEmail(email);
		if(author == null) throw new UsernameNotFoundException(email);
		
		return new User(author.getEmail(), author.getPassword(), new ArrayList<>());
	}

	@Override
	public List<AuthorResponse> findAuthors(int page, int size) {
		if(page > 0) page -=1;
		List<AuthorResponse> authorResponses = new ArrayList<>();
		AuthorResponse authorResponse = null;
		
		Pageable pageable = PageRequest.of(page, size);
		Page<Author> authorPage = authorRepository.findAll(pageable);
		List<Author> storedAuthors = authorPage.getContent();
		for(Author author : storedAuthors) {
			authorResponse = new AuthorResponse();
			BeanUtils.copyProperties(author, authorResponse);
			authorResponses.add(authorResponse);
			authorResponse = null;
		}
		return authorResponses;
	}
}
