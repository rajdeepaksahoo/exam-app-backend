package com.online.exam.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@ToString
public class Questions {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long questionId;

    private String questionStream;

    private String question;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Options options;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Answer answer;
}
