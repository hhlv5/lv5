package com.lv5.lv5.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.lv5.lv5.dto.user.SignupRequestDto;
import com.lv5.lv5.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserController {

	private final UserService userService;

	@PostMapping("/user/signup")
	public String signup(@Valid @RequestBody SignupRequestDto requestDto, BindingResult bindingResult) {
		// Validation 예외처리
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		if (fieldErrors.size() > 0) {
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
			}
			return "Validation엣 ㅓ걸림";
		}

		userService.signup(requestDto);

		return "회원가입 됨";
	}

}