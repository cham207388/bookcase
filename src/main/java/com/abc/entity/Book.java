package com.abc.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description="All book details")
@Entity
@Table(name="book")
public class Book {
	
	@Id
	@Column(name = "id", nullable = false, unique=true)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Integer id;

	@Column(name = "isbn", nullable = false)
	@NotNull(message = "ISBN is required in the format: ISBN-10: 1-2345-1234-1")
	@Pattern(regexp = "^ISBN-10: \\d{1}-\\d{4}-\\d{4}-\\d{1}$")
	@ApiModelProperty(notes="ISBN format is: ISBN-10: 1-2345-1234-1")
	private String isbn;

	@Column(name = "title", nullable = false)
	@NotNull(message = "Title is required")
	@Size(min=3, message="Title should be at least 3 characters")
	@ApiModelProperty(notes="Title must be at least 3 characters")
	private String title;

	@Column(name = "edition", nullable = false)
	@NotBlank(message = "Edition is required")
	@Size(min=7, message="Edition should be at least 7 characters")
	@ApiModelProperty(notes="Edition must be at least 7 characters (1st ed.)")
	private String edition;

	@Column(name = "price", nullable = false)
	@NotNull(message = "Price is required")
	@ApiModelProperty(notes="Price should be in numbers")
	private BigDecimal price;

	@Column(name = "date_published", nullable = false)
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy")
	@Past(message="Published date must be in the past")
	@ApiModelProperty(name="Published date should be in the past")
	private Date datePublished;

	@ManyToOne
	@JsonBackReference
	private Author author;
}

