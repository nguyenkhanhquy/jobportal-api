package com.jobportal.api.model.job;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

@Document(collection = "job_post_activity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JobPostActivity {

    @Id
    private String id;

    private String logo;
    private String title;
    private String company;
    private String salary;
    private String address;
    private String experience;
    private String type;
    private String applicants;
    private String gender;
    private String deadline;
    private String description;
    private String requirements;
    private String benefits;
}
