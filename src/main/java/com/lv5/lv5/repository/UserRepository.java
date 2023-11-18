package com.lv5.lv5.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.lv5.lv5.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByEmail(String email);
}