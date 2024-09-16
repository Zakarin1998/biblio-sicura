package com.example.biblioteca_sicura.service;

import com.example.biblioteca_sicura.model.LoginDto;

public interface AuthService {
    String login(LoginDto loginDto);
}