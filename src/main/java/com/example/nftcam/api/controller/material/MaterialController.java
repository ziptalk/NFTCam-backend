package com.example.nftcam.api.controller.material;

import com.example.nftcam.api.dto.material.response.MaterialCardResponseDto;
import com.example.nftcam.api.dto.material.response.MaterialDetailResponseDto;
import com.example.nftcam.api.dto.util.DataResponseDto;
import com.example.nftcam.api.entity.user.details.UserAccount;
import com.example.nftcam.api.service.material.MaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/material")
@RequiredArgsConstructor
@RestController
public class MaterialController {
    private final MaterialService materialService;

    @GetMapping(value = "/list")
    public ResponseEntity<DataResponseDto<List<MaterialCardResponseDto>>> getHomePageList
            (
                    @AuthenticationPrincipal UserAccount userAccount,
                    @Nullable @RequestParam Long cursor,
                    @PageableDefault(size = 6) Pageable pageable
            )
    {
        return ResponseEntity.ok().body(materialService.getMaterialCardList(userAccount, cursor, pageable));
    }

    @GetMapping(value = "/{materialId}")
    public ResponseEntity<DataResponseDto<MaterialDetailResponseDto>> getMaterialDetail(@AuthenticationPrincipal UserAccount userAccount, @PathVariable Long materialId) {
        return ResponseEntity.ok().body(materialService.getMaterialDetail(userAccount, materialId));
    }

    @GetMapping(value = "/test")
    public ResponseEntity<?> test() {
        String test = materialService.test();
        return ResponseEntity.ok().body(test);
    }
}
