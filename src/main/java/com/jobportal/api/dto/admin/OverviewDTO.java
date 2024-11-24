package com.jobportal.api.dto.admin;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OverviewDTO {

    private long totalJobSeekers;

    private long totalRecruiters;

    private long totalJobPosts;

    private long totalJobApplies;
}
