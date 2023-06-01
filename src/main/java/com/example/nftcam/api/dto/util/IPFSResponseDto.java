package com.example.nftcam.api.dto.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class IPFSResponseDto {
    @JsonProperty("IpfsHash")
    private String IpfsHash;
    @JsonProperty("PinSize")
    private long PinSize;
    @JsonProperty("Timestamp")
    private String Timestamp;

    @Builder
    public IPFSResponseDto(String IpfsHash, long PinSize, String Timestamp) {
        this.IpfsHash = IpfsHash;
        this.PinSize = PinSize;
        this.Timestamp = Timestamp;
    }
}
