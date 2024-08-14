package com.example.dtos;

import com.example.models.Token;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginResponseDto {

	private Token token;
}
