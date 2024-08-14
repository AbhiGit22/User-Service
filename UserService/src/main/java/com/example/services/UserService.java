package com.example.services;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.exceptions.InvalidPasswordException;
import com.example.exceptions.InvalidTokenException;
import com.example.models.Token;
import com.example.models.User;
import com.example.repositories.TokenRepository;
import com.example.repositories.UserRepository;

//Ideally we should create Interface of service and then we implement 
@Service
public class UserService {

	private UserRepository userRepository;
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	private TokenRepository tokenRepository;

	public UserService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, TokenRepository tokenRepository) {
		this.userRepository = userRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.tokenRepository = tokenRepository;

	}

	public User signUp(String email, String password, String name) {

		Optional<User> optionalUser = userRepository.findByEmail(email);

		if (optionalUser.isPresent()) {
			return optionalUser.get();
		}

		User user = new User();
		user.setEmail(email);
		user.setName(name);
		user.setPassword(bCryptPasswordEncoder.encode(password));

		return userRepository.save(user);

	}

	public Token login(String email, String password) throws InvalidPasswordException {

		//1.Check if the user exist or not with given email. 
		//2.if not then either throw exception or redirect them to signup page.
		//3.If present then compare incoming password with password stored in database
		//4.If password matches then return new token
		
		Optional<User> optionalUser = userRepository.findByEmail(email);
		if(optionalUser.isEmpty()) {
			//user with given email is not present
			return null;
		}
		
		User user = optionalUser.get();
		user.setEmailVerified(true);
		
		if(!bCryptPasswordEncoder.matches(password, user.getPassword())) {
			throw new InvalidPasswordException("Please Enter Correct Password");
		}
		//Login Successful, Generate new Token
		Token token = generateToken(user);
		
		Token savedToken = tokenRepository.save(token);
		
		return savedToken;	
	}
	

	public void logOut(String tokenValue) throws InvalidTokenException {
		//validate if token is present in db or not and isDeleted = false;
		
		Optional<Token> optionalToken = tokenRepository.findByValueAndIsDeleted(tokenValue, false);
		if(optionalToken.isEmpty()) {
			throw new InvalidTokenException(tokenValue);
		}
		
		Token token = optionalToken.get();
		token.setDeleted(true);
		
		tokenRepository.save(token);
		
		return;
	}
	
	public User validateToken(String tokenValue) throws InvalidTokenException {

		Optional<Token> optionalToken = tokenRepository.findByValueAndIsDeleted(tokenValue, false);
		
		if(optionalToken.isEmpty()) {
			throw new InvalidTokenException("wrong Token passed");
		}
		return optionalToken.get().getUser();
		
	}
	
	
	

	private Token generateToken(User user) {
		LocalDate currentTime = LocalDate.now();
		LocalDate thirtyDaysFromCurrentTime = currentTime.plusDays(30);
		
		Date expiryDate = Date.from(thirtyDaysFromCurrentTime.atStartOfDay(ZoneId.systemDefault()).toInstant());
		
		Token token = new Token();
		token.setExpiryAt(expiryDate);
		
		//Token value is randomly generating string of 128 characters
		token.setValue(RandomStringUtils.randomAlphanumeric(128));

		//set User
		token.setUser(user);
		
		return token;
	}

	
	
	
	
	
}
