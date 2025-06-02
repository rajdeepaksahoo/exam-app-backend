package com.online.exam.repository;

import com.online.exam.model.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp,String> {
    Optional<Otp> findByOtp(int otp);
}

