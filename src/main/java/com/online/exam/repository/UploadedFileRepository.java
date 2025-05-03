package com.online.exam.repository;

import com.online.exam.model.UploadedFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UploadedFileRepository extends JpaRepository<UploadedFile,Long> {
}
