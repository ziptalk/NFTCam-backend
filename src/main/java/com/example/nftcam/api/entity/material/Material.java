package com.example.nftcam.api.entity.material;

import com.example.nftcam.api.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "NFTCAM_MATERIAL")
public class Material {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id", nullable = false)
    private Long id;

    @Column(name = "material_title", nullable = false)
    private String title;

    @Column(name = "material_source")
    private String source;

    @Column(name = "material_nft_id")
    private String nftId;

    @Column(name = "is_minting", nullable = false)
    private Boolean isMinting;

    @Column(name = "material_taken_device", nullable = false)
    private String device;

    @Column(name = "material_address", nullable = false)
    private String address;

    @Column(name = "taken_at", nullable = false)
    private LocalDateTime takenAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;

    @Builder
    public Material(String title, String source, Boolean isMinting, String device, String address, LocalDateTime takenAt, User user) {
        this.title = title;
        this.source = source;
        this.isMinting = isMinting;
        this.device = device;
        this.address = address;
        this.takenAt = takenAt;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.user = user;
    }

    public static Material of(String title, String source, Boolean isMinting, String device, String address, LocalDateTime takenAt, User user) {
        return Material.builder()
                .title(title)
                .source(source)
                .isMinting(isMinting)
                .device(device)
                .address(address)
                .takenAt(takenAt)
                .user(user)
                .build();
    }

    public void updateNFTId(String nftId) {
        this.nftId = nftId;
        this.isMinting = true;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateImageUrl(String url) {
        this.source = url;
        this.updatedAt = LocalDateTime.now();
    }

    public void updateTitle(String title) {
        this.title = title;
        this.updatedAt = LocalDateTime.now();
    }
}
