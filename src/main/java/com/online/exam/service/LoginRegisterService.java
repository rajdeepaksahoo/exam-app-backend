package com.online.exam.service;

import com.online.exam.model.UserModel;

import java.util.Map;

public interface LoginRegisterService {
    public String login(UserModel user);
    public Map<String,String> register(UserModel user);
}
