package com.example.nftcam.api.dto.wallet.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WalletInfoCreateRequestDto {
    private String walletName;
    private String walletAddress;
}
