package com.jobportal.api.dto.user;

import com.jobportal.api.entity.user.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {

    private int id;
    private String email;
    private String fullName;

    // Phương thức chuyển đổi từ User thành UserDTO
    public static UserDTO fromUser(User user) {
        UserDTO userDTO = new UserDTO();

        userDTO.setId(user.getId());
        userDTO.setEmail(user.getEmail());
        userDTO.setFullName(user.getFullName());

        return userDTO;
    }
}
