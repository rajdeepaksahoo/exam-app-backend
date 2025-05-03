package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long ansId;
    private String answer;
    @OneToOne(mappedBy = "answer")
    @JsonIgnore
    private Questions question;

    @Override
    public String toString() {
        return "Answer{" +
                "ansId='" + ansId + '\'' +
                ", answer='" + answer + '\'' +
                '}';
    }
}
