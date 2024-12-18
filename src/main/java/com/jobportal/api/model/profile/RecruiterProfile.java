package com.jobportal.api.model.profile;

import com.jobportal.api.model.user.User;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "recruiter_profile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class RecruiterProfile {

    @Id
    private String userId;

    @DBRef
    private User user;

    @DBRef
    private Company company;

    private String name;

    private String position;

    private String phone;

    private String recruiterEmail;
}
