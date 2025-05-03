package com.online.exam.service.impl;

import com.online.exam.config.customUserDetails.CustomUserDetails;
import com.online.exam.config.jwt.JwtConfig;
import com.online.exam.model.UserModel;
import com.online.exam.repository.UserModelRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private UserModelRepository userModelRepository;
    private JwtConfig jwtConfig;
    private PasswordEncoder passwordEncoder;
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
    public UserModel registerUser(UserModel userModel){
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        Optional<UserModel> byUsername = userModelRepository.findByUsername(userModel.getUsername());
        if (byUsername.isPresent())
            return new UserModel();
        return userModelRepository.save(userModel);
    }
    public String getToken(UserModel userModel)
    {
        return jwtConfig.generateToken(new CustomUserDetails(userModel));
    }
    public List<UserModel> findAll(){
        return userModelRepository.findAll();
    }
}
