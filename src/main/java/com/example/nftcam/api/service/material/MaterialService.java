package com.example.nftcam.api.service.material;

import com.example.nftcam.api.dto.material.request.MaterialModifyRequestDto;
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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
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
            throw CustomException.builder().httpStatus(HttpStatus.UNPROCESSABLE_ENTITY).message("존재하지 않거나 material 소유자가 아닙니다.").build();
        }

        MaterialDetailResponseDto materialDetailResponseDto = MaterialDetailResponseDto.of(material);

        return DataResponseDto.<MaterialDetailResponseDto>builder()
                .data(materialDetailResponseDto)
                .build();
    }

    @Transactional
    public Long createMaterial(UserAccount userAccount, MaterialSaveRequestDto materialSaveRequestDto) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        User user = userRepository.findById(userAccount.getUserId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않는 user 입니다.").build());
        String coordToAddr = locationConversion.coordToAddr(materialSaveRequestDto.getLongitude(), materialSaveRequestDto.getLatitude());

        Material material = materialRepository.save(Material.builder()
                .isMinting(false)
                .device(materialSaveRequestDto.getDevice())
                .address(coordToAddr)
                .takenAt(LocalDateTime.parse(materialSaveRequestDto.getTakenAt(), formatter))
                .user(user)
                .build());

        return material.getId();
    }

    @Transactional
    public Long updateImageToMaterial(UserAccount userAccount, MultipartFile image, Long materialId){
        String imageUrl = null;
        User user = userRepository.findById(userAccount.getUserId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않는 user 입니다.").build());

        try {
            imageUrl = amazonS3Uploader.saveFileAndGetUrl(image);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("이미지 업로드에 실패했습니다.").build();
        }

        Material material = materialRepository.findByIdAndUser_Id(materialId, user.getId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않거나 material 소유자가 아닙니다.").build());

        material.updateImageUrl(imageUrl);
        return material.getId();
    }

    @Transactional
    public Long updateMaterialTitle(UserAccount userAccount, Long materialId, MaterialModifyRequestDto materialModifyRequestDto) {
        User user = userRepository.findById(userAccount.getUserId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않는 user 입니다.").build());
        Material material = materialRepository.findByIdAndUser_Id(materialId, user.getId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않거나 material 소유자가 아닙니다.").build());
        material.updateTitle(materialModifyRequestDto.getTitle());
        return material.getId();
    }

    @Transactional
    public Long mintingMaterial(UserAccount userAccount, Long materialId) {
        User user = userRepository.findById(userAccount.getUserId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않는 user 입니다.").build());
        Material material = materialRepository.findByIdAndUser_Id(materialId, user.getId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않거나 material 소유자가 아닙니다.").build());
        material.updateNFTId("ON PROGRESS...");
        return material.getId();
    }

    @Transactional
    public void deleteMaterial(UserAccount userAccount, Long materialId) {
        User user = userRepository.findById(userAccount.getUserId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않는 user 입니다.").build());

        Material material = materialRepository.findByIdAndUser_Id(materialId, user.getId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않거나 material 소유자가 아닙니다.").build());

        int lastIndex = material.getSource().lastIndexOf('/')+1;
        String fileName = material.getSource().substring(lastIndex);
        String decodedFileName = null;
        try {
            decodedFileName = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("파일 이름 디코딩에 실패했습니다.").build();
        }
        amazonS3Uploader.deleteFile(decodedFileName);

        materialRepository.delete(material);
    }
}
