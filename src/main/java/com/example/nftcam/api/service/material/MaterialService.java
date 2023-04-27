package com.example.nftcam.api.service.material;

import com.example.nftcam.api.dto.material.response.MaterialCardResponseDto;
import com.example.nftcam.api.dto.material.response.MaterialDetailResponseDto;
import com.example.nftcam.api.dto.util.DataResponseDto;
import com.example.nftcam.api.entity.material.MaterialRepository;
import com.example.nftcam.api.entity.user.UserRepository;
import com.example.nftcam.api.entity.user.details.UserAccount;
import com.example.nftcam.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialService {
    private final UserRepository userRepository;
    private final MaterialRepository materialRepository;

    @Transactional(readOnly = true)
    public DataResponseDto<List<MaterialCardResponseDto>> getMaterialCardList(UserAccount userAccount, Long cursor, Pageable pageable) {
        List<MaterialCardResponseDto> materialCardResponseDtos = materialRepository.findAllByUserIdWithPaging(userAccount.getUserId(), cursor, pageable).stream()
                .map(MaterialCardResponseDto::of)
                .collect(Collectors.toList());

        return DataResponseDto.<List<MaterialCardResponseDto>>builder()
                .data(materialCardResponseDtos)
                .build();
    }

    @Transactional
    public DataResponseDto<MaterialDetailResponseDto> getMaterialDetail(UserAccount userAccount, Long materialId) {
        MaterialDetailResponseDto materialDetailResponseDto = MaterialDetailResponseDto.of(materialRepository.findById(materialId)
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않는 data 입니다.").build()));

        return DataResponseDto.<MaterialDetailResponseDto>builder()
                .data(materialDetailResponseDto)
                .build();
    }
}
