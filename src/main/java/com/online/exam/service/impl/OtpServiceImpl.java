package com.online.exam.service.impl;

import com.online.exam.dto.OtpRequest;
import com.online.exam.dto.UserDto;
import com.online.exam.model.Otp;
import com.online.exam.repository.OtpRepository;
import com.online.exam.service.OtpService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.random.RandomGenerator;

@Service
@AllArgsConstructor
public class OtpServiceImpl implements OtpService {

    private OtpRepository otpRepository;
    private EmailService emailService;

    @Override
    public void sendOtp(UserDto userDto) {
        Integer otp = generateOtp();
        Date validTill = new Date();
        validTill.setMinutes(new Date().getMinutes()+1);
        Otp otoToSave = Otp.builder()
                .createdOn(new Date())
                .modifiedOn(new Date())
                .validTill(validTill)
                .otp(otp)
                .username(userDto.getUsername())
                .build();
        Otp savedOtp = otpRepository.save(otoToSave);
        String body = getMailBody(userDto,savedOtp);
        emailService.sendOtp(userDto.getUsername(),
                "Your One-Time Password (OTP) for Registration",
                body);
    }

    private String getMailBody(UserDto userDto, Otp otp) {
        String body = String.format("""
        Dear %s,
        
        Your One-Time Password (OTP) for verification is:
        
        🔐 OTP: %s
        
        This OTP is valid for the next 5 minutes. Please do not share it with anyone for security reasons.
        
        If you did not request this, please ignore this message or contact our support team immediately.
        
        Thank you,
        Team %s
        """, userDto.getFirstName()+" "+userDto.getLastName(), otp.getOtp(), "Quiz App");

        return body;
    }

    @Override
    public boolean isValidOtp(OtpRequest otpRequest) {
        Optional<Otp> byOtp = otpRepository.findByOtp(otpRequest.getOtp());
        if(byOtp.isPresent()){
            Otp otp = byOtp.get();
            if(!new Date().after(otp.getValidTill())){
                return true;
            }
        }
        return false;
    }

    private Integer generateOtp(){
        RandomGenerator randomGenerator = RandomGenerator.getDefault();
        int otp = randomGenerator.nextInt(999999);
        Optional<Otp> byOtp = otpRepository.findByOtp(otp);
        if(byOtp.isPresent()){
            return generateOtp();
        }
        return otp;
    }

}
