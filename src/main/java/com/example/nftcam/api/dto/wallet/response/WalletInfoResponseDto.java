package com.example.nftcam.api.dto.wallet.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WalletInfoResponseDto {
    private Long walletId;
    private String walletName;
    private String walletAddress;

    @Builder
    public WalletInfoResponseDto(Long walletId, String walletName, String walletAddress) {
        this.walletId = walletId;
        this.walletName = walletName;
        this.walletAddress = walletAddress;
    }

    public static WalletInfoResponseDto of(Long walletId, String walletName, String walletAddress) {
        return WalletInfoResponseDto.builder()
                .walletId(walletId)
                .walletName(walletName)
                .walletAddress(walletAddress)
                .build();
    }
}
