package com.online.exam.service;

import com.online.exam.dto.OtpRequest;
import com.online.exam.dto.UserDto;

public interface OtpService {
    public void sendOtp(UserDto userDto);
    public boolean isValidOtp(OtpRequest otp);
}
