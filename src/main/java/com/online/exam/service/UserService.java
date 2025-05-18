package com.online.exam.service;

import com.online.exam.dto.UserDto;
import com.online.exam.model.UserModel;

public interface UserService {
    public UserModel registerUser(UserDto userDto);
    public String getToken(UserModel userModel);
}
