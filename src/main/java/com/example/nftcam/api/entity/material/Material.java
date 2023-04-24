package com.example.nftcam.api.entity.material;

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

    @Builder
    public Material(String title, Boolean isMinting, String device, String address, LocalDateTime takenAt) {
        this.title = title;
        this.isMinting = isMinting;
        this.device = device;
        this.address = address;
        this.takenAt = takenAt;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Material of(String title, Boolean isMinting, String device, String address, LocalDateTime takenAt) {
        return Material.builder()
                .title(title)
                .isMinting(isMinting)
                .device(device)
                .address(address)
                .takenAt(takenAt)
                .build();
    }

    public void updateNFTId(String nftId) {
        this.nftId = nftId;
        this.isMinting = true;
        this.updatedAt = LocalDateTime.now();
    }
}
