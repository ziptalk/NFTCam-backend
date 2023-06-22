package com.example.nftcam.api.controller.point;

import com.example.nftcam.api.dto.point.request.PointChargeRequestDto;
import com.example.nftcam.api.dto.point.response.PointChargeResponseDto;
import com.example.nftcam.api.entity.user.details.UserAccount;
import com.example.nftcam.api.service.point.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequestMapping("/api/point")
@RequiredArgsConstructor
@RestController
public class PointController {
    private final PointService pointService;

    @GetMapping
    public ResponseEntity<Map<String, Long>> getMyPoint
            (
                    @AuthenticationPrincipal UserAccount userAccount
            )
    {
        Map<String, Long> myPoint = pointService.getMyPoint(userAccount);
        return ResponseEntity.ok().body(myPoint);
    }


    @PatchMapping
    public ResponseEntity<PointChargeResponseDto> chargeMyPoint
            (
                    @AuthenticationPrincipal UserAccount userAccount,
                    @RequestBody PointChargeRequestDto pointChargeRequestDto
            )
    {
        PointChargeResponseDto pointChargeResponseDto = pointService.addPoint(userAccount, pointChargeRequestDto);
        return ResponseEntity.ok().body(pointChargeResponseDto);
    }

}
