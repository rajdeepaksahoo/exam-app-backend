package com.online.exam.controller;

import com.online.exam.dto.OtpRequest;
import com.online.exam.dto.UserDto;
import com.online.exam.service.OtpService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/otp")
@CrossOrigin
public class OtpController {
    private OtpService otpService;

    @PostMapping("/sendOtp")
    public ResponseEntity<Map<String,Object>> sendOtp(@RequestBody UserDto userDto){
        otpService.sendOtp(userDto);
        return ResponseEntity.ok(Map.of("STATUS","Otp Sent Successfully"));
    }

    @PostMapping("/validateOtp")
    public ResponseEntity<Map<String,Object>> validateOtp(@RequestBody OtpRequest otpRequest){
        boolean validOtp = otpService.isValidOtp(otpRequest);
        return ResponseEntity.ok(Map.of("STATUS",validOtp?"Otp Validated Successfully":"Invalid Otp", "isValid",validOtp));
    }
}
