package com.example.vrc.authentication.services.impl;

import com.example.vrc.authentication.DTOs.UserDTO;
import com.example.vrc.authentication.mappers.UserMapper;
import com.example.vrc.authentication.models.UserEntity;
import com.example.vrc.authentication.repositories.UserRepository;
import com.example.vrc.authentication.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
    public void changePassword(String userEmail , String newPassword) {
        Optional<UserEntity> userEntityOpt = this.userRepository.findByEmailIgnoreCase(userEmail);

        if (userEntityOpt.isPresent()) {
            UserEntity userEntity = userEntityOpt.get();
            userEntity.setPassword(newPassword);
            this.userRepository.save(userEntity); // Save the updated user entity
            this.userMapper.toDto(userEntity);
        }

    }
    @Override
    public UserDTO getUserByEmail(String userEmail) {
        Optional<UserEntity> userEntityOpt = this.userRepository.findByEmailIgnoreCase(userEmail);
        return userEntityOpt.map(user -> this.userMapper.toDto(user)).orElse(null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByEmailIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + username));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), Collections.emptyList());
    }
}
