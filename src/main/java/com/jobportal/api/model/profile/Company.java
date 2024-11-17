package com.jobportal.api.model.profile;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "company")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Company {

    @Id
    private String id;

    private String name;

    private String website;

    private String description;

    private String address;

    private String logo;
}
