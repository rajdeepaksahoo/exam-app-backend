package com.online.exam.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Table
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Otp {
    @Id
    private String username;
    private Integer otp;
    private Date createdOn;
    private Date modifiedOn;
    private Date validTill;
}
