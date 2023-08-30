package com.example.nftcam.event;

import com.example.nftcam.api.entity.material.ChainType;
import lombok.Data;

@Data
public class MintingCompletedEvent {
    private String transactionHash;
    private Long materialId;
    private ChainType chainType;

    public MintingCompletedEvent(String transactionHash, Long materialId, ChainType chainType) {
        this.transactionHash = transactionHash;
        this.materialId = materialId;
        this.chainType = chainType;
    }
}
