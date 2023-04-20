package com.example.nftcam.api.entity.user;

import com.example.nftcam.api.entity.token.UserToken;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "NFTCAM_USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "user_uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserToken userToken;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public User(String uuid, Role role) {
        this.uuid = uuid;
        this.role = role;
        this.isActive = true;
    }
}