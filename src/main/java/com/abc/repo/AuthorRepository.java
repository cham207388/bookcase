package com.abc.repo;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.abc.entity.Author;

@Repository
public interface AuthorRepository extends PagingAndSortingRepository<Author, Integer> {
	public Author findByEmail(String email);
}
