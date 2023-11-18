package com.lv5.lv5.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.lv5.lv5.filter.HttpLoggingFilter;
import com.lv5.lv5.jwt.JwtUtil;
import com.lv5.lv5.security.JwtAuthenticationFilter;
import com.lv5.lv5.security.JwtAuthorizationFilter;
import com.lv5.lv5.security.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity // Spring Security 지원을 가능하게 함
@RequiredArgsConstructor
public class WebSecurityConfig {

	private final JwtUtil jwtUtil;
	private final UserDetailsServiceImpl userDetailsService;
	private final AuthenticationConfiguration authenticationConfiguration;

	@Bean
	public AccessDeniedHandlerImpl accessDeniedHandler() {
		return new AccessDeniedHandlerImpl();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public HttpLoggingFilter httpLoggingFilter() {
		HttpLoggingFilter filter = new HttpLoggingFilter();
		return filter;
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
		JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil);
		filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
		return filter;
	}

	@Bean
	public JwtAuthorizationFilter jwtAuthorizationFilter() {
		return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		// CSRF 설정
		http.csrf(AbstractHttpConfigurer::disable);

		// 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
		http.sessionManagement(
			(sessionManagement) -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

		http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests.requestMatchers(
				PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
			.requestMatchers("/").permitAll() // 메인 페이지 요청 허가
			.requestMatchers("/api/user/**").permitAll() // '/api/user/'로 시작하는 요청 모두 접근 허가
			.requestMatchers("/api/admin/**").hasRole("ADMIN") // admin Role
			.anyRequest().authenticated() // 그 외 모든 요청 인증처리
		);

		http.formLogin(Customizer.withDefaults());

		// role 관련 예외 처리
		http.exceptionHandling((exception) -> exception.accessDeniedHandler(accessDeniedHandler()));

		// 필터 관리
		// httpLoggingFilter, JwtAuthorizationFilter 모두 OncePerRequestFilter를 상속받고잇으므로 클래스에 @Order 붙여서 순서 지정
		http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
		// UsernamePasswordAuthenticationFilter 를 상속받는 authentiacationfilter를 그 앞에
		http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}