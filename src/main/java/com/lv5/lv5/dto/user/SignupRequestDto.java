package com.lv5.lv5.dto.user;


import com.lv5.lv5.entity.UserRoleEnum;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupRequestDto {
	@Email
	@NotBlank
	private String username;

	@NotBlank
	private String password;

	@NotBlank
	private String phoneNumber;

	@NotBlank
	private String address;

	@NotNull
	private UserRoleEnum role;
}