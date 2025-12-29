package com.example.project_interview.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.Data;


import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "Forms")
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Form {

    @Id
    private String id;

    private String formTitle;

    private String aliasName;

    private String module;

    private String characteristic;

    private String subCharacteristic;

    private String recurrence;

    private String startMonth;

    private String compliancePeriod;

    private Date effectiveDate;

    private String text;

    private Boolean isActive;

    @OneToMany(
            mappedBy = "form",
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY
    )
    private List<Question> questions;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user;




}
