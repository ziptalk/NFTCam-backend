package com.example.nftcam.api.dto.material.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MaterialMintingRequestDto {
    private String title;
    private String walletAddress;
    // 네트워크는 추후 enum으로 변경하면 좋을 것 같음 -> 네트워크에 따라 다른 컨트랙트 발동하도록
    private String network;
}
