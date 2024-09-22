package com.jobportal.api.model.profile;

import com.jobportal.api.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "job_seeker_profile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class JobSeekerProfile {

    @Id
    private String userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "address")
    private String address;

    @Column(name = "work_experience")
    private String workExperience;

    @Column(name = "avatar")
    private String avatar;

    public JobSeekerProfile(final User user, final String fullName) {
        this.user = user;
        this.fullName = fullName;
    }
}
