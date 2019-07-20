package com.abc.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * This class stores user credentials for signing in
 * @author cham2
 *
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginRequestModel {
	private String email;
	private String password;
}