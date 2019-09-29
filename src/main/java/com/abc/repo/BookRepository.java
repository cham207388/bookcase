package com.abc.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.abc.entity.Book;

@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, Integer> {
	public Book findByIsbn(String isbn);
	public Book findByTitle(String title);
	public void deleteByTitle(String title);
}