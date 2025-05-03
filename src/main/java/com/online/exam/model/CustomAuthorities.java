package com.online.exam.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CustomAuthorities {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long authorityId;
    private String authorityName="USER";
    @ManyToMany(mappedBy = "roles")
    private List<UserModel> userModels;

    @Override
    public String toString() {
        return "[ authorityId : "+authorityName+", authorityName : "+authorityName;
    }
}
