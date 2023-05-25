package com.example.nftcam.api.dto.material.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MaterialMadeResponseDto {
    private Long materialId;

    @Builder
    public MaterialMadeResponseDto(Long materialId) {
        this.materialId = materialId;
    }
}
