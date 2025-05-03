package com.online.exam.repository;

import com.online.exam.model.CustomAuthorities;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<CustomAuthorities,Long> {
}
