package com.abc.service.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.abc.entity.Author;
import com.abc.repo.AuthorRepository;

class AuthorServiceImplTest {
	@Mock
	AuthorRepository authorRepository;
	@Mock
	BCryptPasswordEncoder bCryptPasswordEncoder;
	@Mock
	private RequestAttributes attrs;
	URI uri = null;
	@InjectMocks
	AuthorServiceImpl authorService;
	
	Author author = null;
	Author storedAuthor = null; 
	Optional<Author> authorOptional = null;
	String encodedPassword = "aksjh8301h2oihsoh10iwh192";	

	@BeforeEach
	void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		author = new Author();
		authorOptional = Optional.of(author);
		author.setId(1);
		author.setFirstName("Julie");
		author.setLastName("Seals");
		author.setEmail("julie.seals@abc.com");
		author.setPassword("712isnotSecret4");
		storedAuthor = authorOptional.get();
		author.setBooks(new ArrayList<>());
		uri =  URI.create("localhost:8989/bookcase/authors");
		RequestContextHolder.setRequestAttributes(attrs);
	}

	@Test
	void testFindById() {
		when(authorRepository.findById(anyInt())).thenReturn(authorOptional);
		Author auth = authorService.findById(1);
		assertNotNull(auth);
		assertEquals("Julie", auth.getFirstName());
	}
	
	@Test
	void testFindById_Exception() {
		when(authorRepository.findById(anyInt())).thenReturn(null);
		assertThrows(NullPointerException.class, () -> {
					authorService.findById(0);
				}
			);		
	}
	
	@Test
	void testSave() {
		when(authorRepository.save(any())).thenReturn(author);
		when(bCryptPasswordEncoder.encode(anyString())).thenReturn(encodedPassword);
		
		assertEquals("201 Created", authorService.save(storedAuthor));
	}
	
	@AfterEach
	void tearDown() throws Exception{
		author = null;
		authorOptional = null;
	}
}
