package com.online.exam.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Options {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long optionId;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    @OneToOne(mappedBy = "options")
    @JsonIgnore
    private Questions question;

    @Override
    public String toString() {
        return "Options{" +
                "optionId=" + optionId +
                ", option1='" + option1 + '\'' +
                ", option2='" + option2 + '\'' +
                ", option3='" + option3 + '\'' +
                ", option4='" + option4 + '\'' +
                '}';
    }
}
