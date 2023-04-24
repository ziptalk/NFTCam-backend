package com.example.nftcam.api.controller.material;

import com.example.nftcam.api.service.material.MaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/material")
@RequiredArgsConstructor
@RestController
public class MaterialController {
    private final MaterialService materialService;

    @PostMapping("/convertion")
    public ResponseEntity<?> saveMaterial(
           // @AuthenticationPrincipal UserAccount userAccount, @RequestBody MaterialSaveRequestDto materialSaveRequestDto
    ) {
//        materialService.saveMaterial(userAccount, materialSaveRequestDto);
        materialService.test();
        return ResponseEntity.ok(HttpStatus.CREATED);
    }
}
