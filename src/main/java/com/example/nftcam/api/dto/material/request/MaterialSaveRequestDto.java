package com.example.nftcam.api.dto.material.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MaterialSaveRequestDto {
    private String title;
    private String device;
    private String address;
    private String takenAt;
    private Long latitude;
    private Long longitude;
}
