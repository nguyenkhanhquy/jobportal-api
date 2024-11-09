package com.jobportal.api.dto.request.job;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobPostSearchFilterRequest {

    private int page = 1;
    private int size = 10;

    private String query;
    private String address;
    private String order = "latest";
}
