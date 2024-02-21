package com.example.otpgen.service.impl;

import com.example.otpgen.model.User;
import com.example.otpgen.model.exceptions.*;
import com.example.otpgen.repository.UserRepository;
import com.example.otpgen.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    public final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> findById(Long id){
        return userRepository.findById(id);
    }

    @Override
    public User login(String email, String password) {
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            throw new InvalidArgumentsException();
        }
        if(!this.userRepository.findByEmailEqualsAndPasswordEquals(email, password).isPresent()){
            throw new InvalidUserCredentialsException();
        }

        return (User) userRepository.findByEmailEqualsAndPasswordEquals(email, password)
                .orElseThrow(InvalidUserCredentialsException::new);
    }

    @Override
    public User register(String email, String password, String repeatedPassword, String name, String surname) {
        if (email == null || password == null || email.isEmpty() || password.isEmpty()) {
            throw new InvalidEmailOrPasswordException();
        }

        if (!password.equals(repeatedPassword)) {
            throw new PasswordsDoNotMatchException();
        }

        if(this.userRepository.findByEmail(email).isPresent()) {
            throw new UsernameAlreadyExistsException(email);
        }

        User user = new User(email, password, name, surname);

        userRepository.save(user);
        return user;
    }
}