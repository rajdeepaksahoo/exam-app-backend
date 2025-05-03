package com.online.exam.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "UPLOADED_FILE")
public class UploadedFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String fileName;

    private String fileType;

    @Lob  // Important
    @Column(name = "valid_entry", columnDefinition = "LONGBLOB")
    private byte[] validEntry;

    @Lob  // Important
    @Column(name = "invalid_entry", columnDefinition = "LONGBLOB")
    private byte[] invalidEntry;

    private Date createdOn;

    private long createdBy;

    private Date modifiedOn;

    private long modifiedBy;
}
