package com.abc.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description="All author details")
@Entity
@Table(name="author")
public class Author {
	
	@Id
	@Column(nullable=false, name="id")
	@SequenceGenerator(sequenceName="author_seq", name="a_seq")
	@GeneratedValue(generator="a_seq", strategy=GenerationType.SEQUENCE)
	private Integer id;
	
	@Column(nullable=false, name="first_name")
	@NotBlank(message = "First Name is required")
	@Size(min=3, message="First name must be at least 3 characters")
	@ApiModelProperty(notes="First name must be at least 3 characters")
	private String firstName;
	
	@Column(name="last_name")
	@NotNull(message = "Last Name is required")
	@Size(min=3, message="Last name must be at least 3 characters")
	@ApiModelProperty(notes="last name must be at least 3 characters")
	private String lastName;
	
	@Column(name = "email", unique = true)
	@NotNull(message="email cannot be null")
	@Email
	@Pattern(regexp="[A-Za-z0-9._%-+]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}")
	@ApiModelProperty(notes="email must follow proper format")
	private String email;
	
	@NotNull(message="Password")
	@Size(min=8, message="Password must be >= 8")
	@ApiModelProperty(notes="password must be at least 8 characters")
	private String password;
	
	@OneToMany(cascade = CascadeType.ALL,
			fetch = FetchType.EAGER) //this might change to EAGER
	@JoinColumn(name="author_id")
	@JsonManagedReference
	private List<Book> books = new ArrayList<Book>();
	

	public void addBook(Book book) {
		books.add(book);
		book.setAuthor(this);
	}
	
	public void removeBook(Book book) {
		books.remove(book);
		book.setAuthor(null);
	}
}
