package com.lv5.lv5.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lv5.lv5.dto.user.LoginRequestDto;
import com.lv5.lv5.jwt.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtAuthenticationFilter")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final JwtUtil jwtUtil;

	public JwtAuthenticationFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
		setFilterProcessesUrl("/api/user/login");
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws
		AuthenticationException {
		try {
			LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
			log.info("requestDto : " + requestDto);

			return getAuthenticationManager().authenticate(
				new UsernamePasswordAuthenticationToken(
					requestDto.getUsername(),
					requestDto.getPassword(),
					null
				)
			);
		} catch (IOException e) {
			log.error(e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
		Authentication authResult) {
		String username = ((UserDetailsImpl)authResult.getPrincipal()).getUsername();

		// TODO: 2023-11-18 User Role 처리
		//UserRoleEnum role = ((UserDetailsImpl)authResult.getPrincipal()).getUser().getRole();

		//String token = jwtUtil.createToken(username, role);
		//log.info("token : " + token);
		//response.addHeader(JwtUtil.AUTHORIZATION_HEADER, token);
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) {
		response.setStatus(401);
	}

}