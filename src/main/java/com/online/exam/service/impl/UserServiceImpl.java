package com.online.exam.service.impl;

import com.online.exam.config.customUserDetails.CustomUserDetails;
import com.online.exam.config.jwt.JwtConfig;
import com.online.exam.dto.UserDto;
import com.online.exam.enums.Authorities;
import com.online.exam.exception.QAppException;
import com.online.exam.mappers.UserMapper;
import com.online.exam.model.CustomAuthorities;
import com.online.exam.model.UserModel;
import com.online.exam.repository.AuthorityRepository;
import com.online.exam.repository.UserModelRepository;
import com.online.exam.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private UserModelRepository userModelRepository;
    private JwtConfig jwtConfig;
    private PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private AuthorityRepository authorityRepository;
    public PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }
    @Override
    public UserModel registerUser(UserDto userDto){
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Optional<UserModel> byUsername = userModelRepository.findByUsername(userDto.getUsername());
        if (byUsername.isPresent())
            throw new QAppException("User With Same Email Id Already Registered.");
        UserModel userModel = userMapper.toEntity(userDto);
        if(userModel.getRoles()==null || userModel.getRoles().size()==0){
            List<UserModel> userModels = List.of(userModel);
            CustomAuthorities customAuthorities = new CustomAuthorities(0l, Authorities.USER,userModels);
            customAuthorities = authorityRepository.save(customAuthorities);
            userModel.setRoles(List.of(customAuthorities));
        }
        return userModelRepository.save(userModel);
    }
    public String getToken(UserModel userModel) {
        return jwtConfig.generateToken(new CustomUserDetails(userModel));
    }
}
