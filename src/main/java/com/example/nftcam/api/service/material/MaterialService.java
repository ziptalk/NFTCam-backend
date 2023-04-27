package com.example.nftcam.api.service.material;

import com.example.nftcam.api.dto.material.request.MaterialSaveRequestDto;
import com.example.nftcam.api.dto.material.response.MaterialCardResponseDto;
import com.example.nftcam.api.dto.material.response.MaterialDetailResponseDto;
import com.example.nftcam.api.dto.util.DataResponseDto;
import com.example.nftcam.api.entity.material.Material;
import com.example.nftcam.api.entity.material.MaterialRepository;
import com.example.nftcam.api.entity.user.User;
import com.example.nftcam.api.entity.user.UserRepository;
import com.example.nftcam.api.entity.user.details.UserAccount;
import com.example.nftcam.exception.custom.CustomException;
import com.example.nftcam.utils.AmazonS3Uploader;
import com.example.nftcam.utils.LocationConversion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialService {
    private final UserRepository userRepository;
    private final MaterialRepository materialRepository;
    private final LocationConversion locationConversion;
    private final AmazonS3Uploader amazonS3Uploader;

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
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않는 data 입니다.").build());

        if (!material.getUser().getId().equals(userAccount.getUserId())) {
            throw CustomException.builder().httpStatus(HttpStatus.UNPROCESSABLE_ENTITY).message("해당 data에 대한 권한이 없습니다.").build();
        }

        MaterialDetailResponseDto materialDetailResponseDto = MaterialDetailResponseDto.of(material);

        return DataResponseDto.<MaterialDetailResponseDto>builder()
                .data(materialDetailResponseDto)
                .build();
    }

    @Transactional
    public void createMaterial(UserAccount userAccount, MultipartFile image, MaterialSaveRequestDto materialSaveRequestDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String imageUrl = null;
        User user = userRepository.findById(userAccount.getUserId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않는 user 입니다.").build());

        try {
            imageUrl = amazonS3Uploader.saveFileAndGetUrl(image);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("이미지 업로드에 실패했습니다.").build();
        }
        String coordToAddr = locationConversion.coordToAddr(materialSaveRequestDto.getLongitude(), materialSaveRequestDto.getLatitude());

        materialRepository.save(Material.builder()
                            .title(materialSaveRequestDto.getTitle())
                            .source(imageUrl)
                            .isMinting(false)
                            .device(materialSaveRequestDto.getDevice())
                            .address(coordToAddr)
                            .takenAt(LocalDateTime.parse(materialSaveRequestDto.getTakenAt(), formatter))
                            .user(user)
                            .build());
    }
}
