package com.jobportal.api.model.job;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "job_post_activity")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JobPostActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
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
