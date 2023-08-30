package com.example.nftcam.api.entity.material;

public enum ChainType {
    SEPOLIA("SEPOLIA"),
    MUMBAI("MUMBAI");

    private String chainType;

    ChainType(String chainType) {
        this.chainType = chainType;
    }

    public String getChainType() {
        return this.chainType;
    }
}

