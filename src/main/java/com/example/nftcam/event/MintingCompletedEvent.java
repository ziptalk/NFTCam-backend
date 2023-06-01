package com.example.nftcam.event;

import lombok.Data;

@Data
public class MintingCompletedEvent {
    private String transactionHash;
    private Long materialId;

    public MintingCompletedEvent(String transactionHash, Long materialId) {
        this.transactionHash = transactionHash;
        this.materialId = materialId;
    }
}
