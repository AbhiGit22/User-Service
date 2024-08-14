package com.example.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.dtos.LogInRequestDto;
import com.example.dtos.LoginResponseDto;
import com.example.dtos.LogoutRequestDto;
import com.example.dtos.SignUpRequestDto;
import com.example.dtos.UserDto;
import com.example.exceptions.InvalidPasswordException;
import com.example.exceptions.InvalidTokenException;
import com.example.models.Token;
import com.example.models.User;
import com.example.services.UserService;

import jakarta.websocket.server.PathParam;

@RestController
@RequestMapping("/users")
public class UserController {

	private UserService userService;

	UserController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/signup")
	public UserDto signUp(@RequestBody SignUpRequestDto requestDto) {
		User user = userService.signUp(requestDto.getEmail(), requestDto.getPassword(), requestDto.getName());

		// get UserDto from user
		return UserDto.from(user);

	}

	@PostMapping("/login")
	public LoginResponseDto login(@RequestBody LogInRequestDto requestDto) throws InvalidPasswordException {

		Token token = userService.login(requestDto.getEmail(), requestDto.getPassword());
		LoginResponseDto responseDto = new LoginResponseDto();
		responseDto.setToken(token);

		return responseDto;
	}

	//we can use path variable also instead of requestBody
	@PostMapping("/logout")
	public ResponseEntity<Void> logOut(@RequestBody LogoutRequestDto requestDto) throws InvalidTokenException {

		ResponseEntity<Void> response = null;
		try {
			userService.logOut(requestDto.getToken());
		}catch (Exception e) {
			System.out.println("Something went wrong");
			 response = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return response;

	}
	
	@PostMapping("/validate/{token}")
	public UserDto validateToken(@PathVariable String token) throws InvalidTokenException {
		return UserDto.from(userService.validateToken(token));
		
		
		
	}

}
