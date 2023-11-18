package com.lv5.lv5.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.lv5.lv5.dto.user.SignupRequestDto;
import com.lv5.lv5.entity.User;
import com.lv5.lv5.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public void signup(SignupRequestDto requestDto) {
		String username = requestDto.getUsername();
		String password = passwordEncoder.encode(requestDto.getPassword());

		// 회원 중복 확인
		Optional<User> checkUsername = userRepository.findByUsername(username);
		if (checkUsername.isPresent()) {
			throw new IllegalArgumentException("중복된 사용자가 존재합니다.");
		}

		requestDto.setPassword(password);

		// 사용자 등록
		User user = new User(requestDto);
		userRepository.save(user);
	}

}