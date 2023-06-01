package com.example.nftcam.api.entity.material;

public enum MintState {
    NONE("NONE"),
    MINTING("MINTING"),
    MINTED("MINTED");

    private String state;

    MintState(String state) {
        this.state = state;
    }

    public String getState() {
        return this.state;
    }
}

