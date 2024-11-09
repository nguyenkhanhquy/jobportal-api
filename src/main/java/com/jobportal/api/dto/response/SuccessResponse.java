package com.jobportal.api.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuccessResponse<T> {

    @Builder.Default
    private boolean success = true;

    @Builder.Default
    private int statusCode = 200;

    private String message;

    private PageInfo pageInfo;

    private T result;

    @Getter
    @Builder
    public static class PageInfo {
        private int currentPage;
        private int totalPages;
        private int pageSize;
        private long totalElements;
        private boolean hasPreviousPage;
        private boolean hasNextPage;
    }
}
