package com.jobportal.api.model.job;

import com.jobportal.api.model.profile.Company;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "job_post")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JobPost {

    @Id
    private String id;

    private String title;

    private String type;

    private String remote;

    private String description;

    private String salary;

    private int quantity;

    private Date createdDate;

    private Date updatedDate;

    private Date expiryDate;

    private String jobPosition;

    private String requirements;

    private String benefits;

    @DBRef
    private Company company;

    private String address;

    private List<JobApply> jobApplies = new ArrayList<>();
}
