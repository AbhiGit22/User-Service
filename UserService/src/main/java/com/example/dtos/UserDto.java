package com.example.dtos;

import java.util.List;

import com.example.models.Role;
import com.example.models.User;

import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {

	private String name;
	private String email;
	@ManyToMany
	private List<Role> roles;
	private boolean isEmailVerified;
	
	public static UserDto from(User user) {
		UserDto dto = new UserDto();
		dto.setEmail(user.getEmail());
		dto.setName(user.getName());
		dto.setEmailVerified(user.isEmailVerified());
		dto.setRoles(user.getRoles());
		
		return dto;
	}
}
