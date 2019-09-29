package com.abc.model.response;

import java.util.Date;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookResponse {
	private Integer id;
	private String isbn;
	private String title;
	private String edition;
	private BigDecimal price;
	private Date datePublished;
	private String fullName;
}
