package com.example.nftcam.api.entity.user.details;

import com.example.nftcam.api.entity.user.Role;
import lombok.Getter;

import java.util.ArrayList;

@Getter
public class UserAccount extends org.springframework.security.core.userdetails.User {

    private Long userId;

    private String uuid;

    private Role role;

    public UserAccount(Long userId, String uuid, Role role) {
        super(uuid, uuid, new ArrayList<Role>() {{
            add(role);
        }});
        this.userId = userId;
        this.uuid = uuid;
        this.role = role;
    }
}
