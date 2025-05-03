package com.online.exam.repository;

import com.online.exam.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserModelRepository extends JpaRepository<UserModel,Long> {
//    @Query("SELECT UM FROM UserModel UM WHERE UM.username=:username")
    Optional<UserModel> findByUsername(String username);
}
