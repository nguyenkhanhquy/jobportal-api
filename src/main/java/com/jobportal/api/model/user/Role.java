package com.jobportal.api.model.user;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DBRef;
import lombok.*;

import java.util.List;

@Document(collection = "role")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Role {

    @Id
    private String id;

    private String name;

    @DBRef  // Để tạo liên kết với các user, MongoDB sử dụng @DBRef
    @ToString.Exclude
    private List<User> users;  // Danh sách người dùng gắn liền với vai trò này
}
