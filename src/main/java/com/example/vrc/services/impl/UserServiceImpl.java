package com.example.vrc.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.vrc.mappers.UserMapper;
import com.example.vrc.models.UserEntity;
import com.example.vrc.repositories.UserRepository;
import com.example.vrc.DTOs.UserDTO;
import com.example.vrc.services.UserService;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDTO createUser(UserDTO userDTO) {
        UserEntity userEntity = this.userRepository.save(this.userMapper.toEntity(userDTO));
        return this.userMapper.toDto(userEntity);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        Optional<UserEntity> userEntityOpt = this.userRepository.findById(userId);
        return userEntityOpt.map(user -> this.userMapper.toDto(user)).orElse(null);
    }

    @Override
    public UserDTO getUserByEmail(String userEmail) {
        Optional<UserEntity> userEntityOpt = this.userRepository.findByEmail(userEmail);
        return userEntityOpt.map(user -> this.userMapper.toDto(user)).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
