package com.jobportal.api.dto.page;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {

    @Builder.Default
    private boolean success = true;

    private String message;

    @Builder.Default
    private int statusCode = 200;

    // Đối tượng page chứa các thông tin về phân trang
    private PageInfo page;

    private List<T> result;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PageInfo {
        private int pageNumber;
        private int size;
        private long totalElements;
        private int totalPages;
    }
}
