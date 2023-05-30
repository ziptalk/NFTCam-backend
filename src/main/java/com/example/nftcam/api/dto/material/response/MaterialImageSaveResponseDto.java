package com.example.nftcam.api.dto.material.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MaterialImageSaveResponseDto {
    private String imageUrl;

    @Builder
    public MaterialImageSaveResponseDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
