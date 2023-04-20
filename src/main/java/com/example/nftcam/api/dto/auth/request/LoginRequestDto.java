package com.example.nftcam.api.dto.auth.request;

import com.example.nftcam.api.entity.user.Role;
import com.example.nftcam.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequestDto {
    private String uuid;

    public User toEntity() {
        return User.builder()
                .uuid(uuid)
                .role(Role.ROLE_USER)
                .build();
    }
}
