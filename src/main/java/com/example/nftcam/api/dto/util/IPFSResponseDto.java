package com.example.nftcam.api.dto.util;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class IPFSResponseDto {
    private String IpfsHash;
    private long PinSize;
    private String Timestamp;

    @Builder
    public IPFSResponseDto(String IpfsHash, long PinSize, String Timestamp) {
        this.IpfsHash = IpfsHash;
        this.PinSize = PinSize;
        this.Timestamp = Timestamp;
    }
}
