package com.lv5.lv5.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.lv5.lv5.entity.User;
import com.lv5.lv5.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "UserDetailsServiceImpl")
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("Not Found " + username));
		log.info("user.getUsername() : " + user.getUsername());
		return new UserDetailsImpl(user);
	}
}