package com.lv5.lv5.entity;

import com.lv5.lv5.dto.user.SignupRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "users")
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, unique = true)
	private String username; // jwt때문에 email을 username으로

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String phoneNumber;

	@Column(nullable = false)
	private String address;

	@Column(nullable = false)
	@Enumerated(value = EnumType.STRING)
	private UserRoleEnum role;

	public User(SignupRequestDto req) {
		this.username = req.getUsername();
		this.password = req.getPassword();
		this.phoneNumber = req.getPhoneNumber();
		this.address = req.getAddress();
		this.role = req.getRole();
	}
}