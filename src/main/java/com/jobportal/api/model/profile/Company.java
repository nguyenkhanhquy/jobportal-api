package com.jobportal.api.model.profile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "company")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Company {

    @Id
    private String id;

    private String name;

    private String website;

    private String description;

    private String address;

    private String logo;
}
