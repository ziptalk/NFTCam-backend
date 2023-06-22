package com.example.nftcam.api.dto.wallet.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class WalletInfoUpdateRequestDto {
    private String walletName;
    private String walletAddress;
}
