package com.lv5.lv5.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
	public ResponseEntity<String> signup(@Valid @RequestBody SignupRequestDto requestDto) {
		try {
			userService.signup(requestDto);
		} catch (Exception e) {
			return ResponseEntity.badRequest()
				.body(e.toString());
		}
		return ResponseEntity.ok()
			.body("회원가입 되었습니다.");
	}

}