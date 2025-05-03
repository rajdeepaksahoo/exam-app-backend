package com.online.exam.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long userId;
    private String username;
    private String password;
    private String confirmPassword;
    private String firstName;
    private String lastName;
    private String gender;
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private List<CustomAuthorities> roles;
}