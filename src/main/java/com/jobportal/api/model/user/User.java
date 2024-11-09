package com.jobportal.api.model.user;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {

    @Id
    private String id;

    private String email;

    private String password;

    private boolean isActive;

    private Date registrationDate;

    @DBRef  // Để tạo liên kết với Role qua tham chiếu
    private Role role;  // Vai trò của người dùng
}
