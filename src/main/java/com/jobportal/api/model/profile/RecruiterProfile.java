package com.jobportal.api.model.profile;

import com.jobportal.api.model.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "recruiter_profile")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RecruiterProfile {

    @Id
    private String userId;

    @OneToOne
    @JoinColumn(name = "user_id")
    @MapsId
    private User user;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "address")
    private String address;

    @Column(name = "company")
    private String company;

    public RecruiterProfile(final User user, final String fullName) {
        this.user = user;
        this.fullName = fullName;
    }
}
