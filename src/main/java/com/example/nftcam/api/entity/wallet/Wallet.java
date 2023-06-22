package com.example.nftcam.api.entity.wallet;

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
@Table(name = "NFTCAM_WALLET")
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wallet_id", nullable = false)
    private Long id;

    @Column(name = "wallet_name", nullable = false, length = 50)
    private String walletName;

    @Column(name = "wallet_address", nullable = false, length = 50)
    private String walletAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Builder
    public Wallet(String walletName, String walletAddress, User user) {
        this.walletName = walletName;
        this.walletAddress = walletAddress;
        this.user = user;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateWalletInfo(String walletName, String walletAddress) {
        this.walletName = walletName;
        this.walletAddress = walletAddress;
    }
}
