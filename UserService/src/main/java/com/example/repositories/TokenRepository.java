package com.example.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.models.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
	@Override
	Token save(Token token); 
	
	//select * from tokens where value = <> and is_deleted = false;
	
	Optional<Token> findByValueAndIsDeleted(String value, boolean isDeleted);

}
