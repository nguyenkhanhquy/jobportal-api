package com.jobportal.api.model.job;

import com.jobportal.api.model.profile.JobSeekerProfile;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "job_saved")
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
    private JobPost jobPost;

    @DBRef
    private JobSeekerProfile jobSeekerProfile;

    private Date savedDate;
}
