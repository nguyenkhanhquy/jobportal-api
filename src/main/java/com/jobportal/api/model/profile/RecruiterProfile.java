package com.jobportal.api.model.profile;

import com.jobportal.api.model.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "recruiter_profile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecruiterProfile {

    @Id
    private String userId;

    // Tham chiếu đến tài liệu User
    @DBRef
    private User user;

    private String fullName;
    private String address;
    private String company;
    private String avatar;
}
