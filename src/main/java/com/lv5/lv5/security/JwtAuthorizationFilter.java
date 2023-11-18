package com.lv5.lv5.security;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import com.lv5.lv5.jwt.JwtUtil;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtAuthorizationFilter")
@Order(1)
public class JwtAuthorizationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;

	public JwtAuthorizationFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws
		ServletException,
		IOException {

		String tokenValue = jwtUtil.getJwtFromHeader(req);
		log.info("tokenValue : " + tokenValue);
		if (StringUtils.hasText(tokenValue)) {
			if (!jwtUtil.validateToken(tokenValue)) {
				log.error("tokenValue :" + tokenValue);
				return;
			}

			Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

			try {
				setAuthentication(info.getSubject());
			} catch (Exception e) {
				log.error("e.getMessage() :" + e.getMessage());
				return;
			}
		}

		filterChain.doFilter(req, res);
	}

	// 인증 처리
	public void setAuthentication(String username) {
		SecurityContext context = SecurityContextHolder.createEmptyContext();
		Authentication authentication = createAuthentication(username);
		context.setAuthentication(authentication);
		log.info("context : " + context);
		SecurityContextHolder.setContext(context);
	}

	// 인증 객체 생성
	private Authentication createAuthentication(String username) {
		UserDetails userDetails = userDetailsService.loadUserByUsername(username);
		log.info("userDetails : " + userDetails);
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
