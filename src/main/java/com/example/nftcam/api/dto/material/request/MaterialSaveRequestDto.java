package com.example.nftcam.api.dto.material.request;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MaterialSaveRequestDto {
    private String device;
    private String takenAt;
    private String latitude;
    private String longitude;

}
