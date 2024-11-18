package com.jobportal.api.model.profile;

import com.jobportal.api.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "job_seeker_profile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JobSeekerProfile {

    @Id
    private String userId;

    // Tham chiếu đến tài liệu User
    @DBRef
    private User user;

    private String fullName;
    private String address;
    private String phone;
    private String dob;
    private String workExperience;
    private String avatar;

    public JobSeekerProfile(User user, String fullName) {
        this.user = user;
        this.fullName = fullName;
    }
}