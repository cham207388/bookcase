package com.abc.model.response;

import java.util.List;

import com.abc.entity.Book;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponse {
	private Integer id;
	private String firstName;
	private String lastName;
	private String email;
	private List<Book> books;
}
