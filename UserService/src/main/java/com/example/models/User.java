package com.example.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class User extends BaseModel {
	private String name;
	
	private String email;
	
	private String Password;
	
	@ManyToMany
	private List<Role> roles;
	
	private boolean isEmailVerified;
	
}
