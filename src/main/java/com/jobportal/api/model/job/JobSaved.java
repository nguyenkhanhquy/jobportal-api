package com.jobportal.api.model.job;

import com.jobportal.api.model.profile.JobSeekerProfile;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "job_save")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class JobSaved {

    @Id
    private String id;

    @DBRef
    private JobPostActivity jobPostActivity;

    @DBRef
    private JobSeekerProfile jobSeekerProfile;
}
