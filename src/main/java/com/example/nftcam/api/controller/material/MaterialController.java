package com.example.nftcam.api.controller.material;

import com.example.nftcam.api.dto.material.request.MaterialModifyRequestDto;
import com.example.nftcam.api.dto.material.request.MaterialSaveRequestDto;
import com.example.nftcam.api.dto.material.response.MaterialCardResponseDto;
import com.example.nftcam.api.dto.material.response.MaterialDetailResponseDto;
import com.example.nftcam.api.dto.util.DataResponseDto;
import com.example.nftcam.api.entity.user.details.UserAccount;
import com.example.nftcam.api.service.material.MaterialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Slf4j
@RequestMapping("/api/material")
@RequiredArgsConstructor
@RestController
public class MaterialController {
    private final MaterialService materialService;

    @PostMapping(value = "/save/image/{materialId}")
    public ResponseEntity<Void> updateMaterialImage(@AuthenticationPrincipal UserAccount userAccount, @RequestPart MultipartFile image, @PathVariable Long materialId) {
        Long newResourceId = materialService.updateImageToMaterial(userAccount, image, materialId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/api/material/{materialId}")
                .buildAndExpand(newResourceId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PostMapping(value = "/save/content")
    public ResponseEntity<Void> createMaterial(@AuthenticationPrincipal UserAccount userAccount,
                                            @RequestBody MaterialSaveRequestDto materialSaveRequestDto) {
        Long material = materialService.createMaterial(userAccount, materialSaveRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/api/material/save/image/{materialId}")
                .buildAndExpand(material)
                .toUri();
        return ResponseEntity.status(HttpStatus.SEE_OTHER).location(location).build();
    }


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

    @PutMapping(value = "/title/{materialId}")
    public ResponseEntity<Void> updateMaterialTitle(@AuthenticationPrincipal UserAccount userAccount, @PathVariable Long materialId, @RequestBody MaterialModifyRequestDto materialModifyRequestDto) {
        Long updatedResourceId = materialService.updateMaterialTitle(userAccount, materialId, materialModifyRequestDto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/api/material/{materialId}")
                .buildAndExpand(updatedResourceId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping(value = "/mint/{materialId}")
    public ResponseEntity<Void> mintingMaterial(@AuthenticationPrincipal UserAccount userAccount, @PathVariable Long materialId) {
        Long updatedResourceId = materialService.mintingMaterial(userAccount, materialId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .replacePath("/api/material/{materialId}")
                .buildAndExpand(updatedResourceId)
                .toUri();
        return ResponseEntity.status(HttpStatus.ACCEPTED).location(location).build();
    }

    @DeleteMapping(value = "/{materialId}")
    public ResponseEntity<Void> deleteMaterial(@AuthenticationPrincipal UserAccount userAccount, @PathVariable Long materialId) {
        materialService.deleteMaterial(userAccount, materialId);
        return ResponseEntity.noContent().build();
    }
}
