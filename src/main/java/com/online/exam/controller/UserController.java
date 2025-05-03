package com.online.exam.controller;

import com.online.exam.exception.InvalidUsernameException;
import com.online.exam.model.UserModel;
import com.online.exam.service.impl.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
@CrossOrigin("*")
public class UserController {
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity< Map<String,String>> registerUser (@RequestBody UserModel userModel){
        if(userModel.getUsername().equals("") || userModel.getUsername().equals("")) throw new InvalidUsernameException("Username Or Password Should not be blank ");

        UserModel userModel1 = userService.registerUser(userModel);
        Map<String,String> response = new HashMap<>();
        response.put("STATUS",userModel1.getUserId()!=null ?userModel.getUsername()+" Registered Successfully":"Registration Failed");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/")
    public ResponseEntity<Object> getDetails (){

        return new ResponseEntity<>("Welcome To JWT Authentication", HttpStatus.CREATED);
    }
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object> admin (){
        return new ResponseEntity<>(SecurityContextHolder.getContext().getAuthentication().getPrincipal(), HttpStatus.CREATED);

    }
    static int a =100;
    @GetMapping("/postAuthorize")
    @PostAuthorize("hasRole('USER')")
    public ResponseEntity<Object> postAuthorize (){
        a++;
        System.out.println(":)  "+a);
        return new ResponseEntity<>("Post Authorize", HttpStatus.CREATED);

    }
    @PostMapping("/token")
    public ResponseEntity<Map<String,String>> getToken (@RequestBody UserModel userModel){
        Map<String,String> response = new HashMap<>();
        if(userModel.getUsername().equals("") || userModel.getUsername().equals("")) throw new InvalidUsernameException("Username Or Password Should not be blank ");
        response.put("token",userService.getToken(userModel));
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping("/preFilter")
    @PreFilter("filterObject.contains('a')")
    public List<String> preFilter(@RequestBody List<String> strings){

        return strings;
    }
}
