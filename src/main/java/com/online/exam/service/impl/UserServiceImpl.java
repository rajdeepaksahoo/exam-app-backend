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
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private UserModelRepository userModelRepository;
    private JwtConfig jwtConfig;
    private PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private AuthorityRepository authorityRepository;
    private final EmailService emailService;
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
        UserModel model = userModelRepository.save(userModel);
        userDto.setUserId(userModel.getUserId());
        try {
            sendNotificationForRegistrationSuccess(userDto);
        }catch (Exception e){
            log.error("Error While Ending Notification :: {}",e.getMessage());
        }
        return model;
    }

    private void sendNotificationForRegistrationSuccess(UserDto model) {
        try {
            String body = """
Dear %s,

We are pleased to inform you that your registration has been successfully completed.

Registration Details:

Name: %s
Email: %s
Registration ID: %s

If you have any questions or require further assistance, feel free to contact us.

Thank you for registering with us!

Best regards,
%s
%s
%s
""";
            String userName = model.getFirstName();
            String email = model.getUsername();
            String registrationId = model.getUserId().toString();
            String senderName = "Support Team Exam Application";
            String organization = "Go Logical";
            String contact = "support@examapp.com";

            String formattedEmail = String.format(body, userName, userName, email, registrationId, senderName, organization, contact);
            System.out.println(formattedEmail);

            emailService.sendEmailWithoutAttachment(model.getUsername(),"Registration status",formattedEmail);
        } catch (MessagingException e) {

        }
    }

    public String getToken(UserModel userModel) {
        return jwtConfig.generateToken(new CustomUserDetails(userModel));
    }
}
