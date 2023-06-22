package com.example.nftcam.api.dto.point.request;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointChargeRequestDto {
    private Long point;

}
