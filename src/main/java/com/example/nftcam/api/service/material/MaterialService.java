package com.example.nftcam.api.service.material;

import com.example.nftcam.api.dto.material.request.MaterialMintingRequestDto;
import com.example.nftcam.api.dto.material.request.MaterialModifyRequestDto;
import com.example.nftcam.api.dto.material.request.MaterialSaveRequestDto;
import com.example.nftcam.api.dto.material.response.MaterialCardResponseDto;
import com.example.nftcam.api.dto.material.response.MaterialDetailResponseDto;
import com.example.nftcam.api.dto.material.response.MaterialImageSaveResponseDto;
import com.example.nftcam.api.dto.util.DataResponseDto;
import com.example.nftcam.api.entity.material.Material;
import com.example.nftcam.api.entity.material.MaterialRepository;
import com.example.nftcam.api.entity.material.MintState;
import com.example.nftcam.api.entity.user.User;
import com.example.nftcam.api.entity.user.UserRepository;
import com.example.nftcam.api.entity.user.details.UserAccount;
import com.example.nftcam.api.service.pinata.PinataService;
import com.example.nftcam.exception.custom.CustomException;
import com.example.nftcam.utils.AmazonS3Uploader;
import com.example.nftcam.utils.LocationConversion;
import com.example.nftcam.web3.NFTCAM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialService {
    private final NFTCAM nft;

    @Value("${metamask.WALLET_ADDRESS}")
    private String WALLET_ADDRESS;

    @Value("${pinata.jwt}")
    private String PINATA_JWT;
    private final UserRepository userRepository;
    private final MaterialRepository materialRepository;
    private final MaterialAsyncService materialAsyncService;
    private final PinataService pinataService;
    private final LocationConversion locationConversion;
    private final AmazonS3Uploader amazonS3Uploader;

    public DataResponseDto<List<MaterialCardResponseDto>> getMaterialCardList(UserAccount userAccount, Long cursor, Pageable pageable) {
        List<MaterialCardResponseDto> materialCardResponseDtos = materialRepository.findAllByUserIdWithPaging(userAccount.getUserId(), cursor, pageable).stream()
                .map(MaterialCardResponseDto::of)
                .collect(Collectors.toList());

        return DataResponseDto.<List<MaterialCardResponseDto>>builder()
                .data(materialCardResponseDtos)
                .build();
    }

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
                .isMinting(MintState.NONE)
                .source(materialSaveRequestDto.getImageUrl())
                .device(materialSaveRequestDto.getDevice())
                .address(coordToAddr)
                .takenAt(LocalDateTime.parse(materialSaveRequestDto.getTakenAt(), formatter))
                .user(user)
                .build());

        return material.getId();
    }

    public MaterialImageSaveResponseDto saveMaterialImage(UserAccount userAccount, MultipartFile image){
        String imageUrl = null;
        User user = userRepository.findById(userAccount.getUserId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않는 user 입니다.").build());

        try {
            imageUrl = amazonS3Uploader.saveFileAndGetUrl(image);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("이미지 업로드에 실패했습니다.").build();
        }

        MaterialImageSaveResponseDto materialImageSaveResponseDto = MaterialImageSaveResponseDto.builder()
                .imageUrl(imageUrl)
                .build();
        return materialImageSaveResponseDto;
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

    @Transactional // 비동기 처리가 안되는 상황 해결해야함
    public Long mintingMaterial(UserAccount userAccount, Long materialId, MaterialMintingRequestDto materialMintingRequestDto) {
        User user = userRepository.findById(userAccount.getUserId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않는 user 입니다.").build());
        Material material = materialRepository.findByIdAndUser_Id(materialId, user.getId())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않거나 material 소유자가 아닙니다.").build());

        if (material.getIsMinting().equals(MintState.MINTING) || material.getIsMinting().equals(MintState.MINTED)) {
            throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("이미 minting 된 material 입니다.").build();
        }

        material.updateOnProgress(materialMintingRequestDto.getTitle(), "ON PROGRESS...");

        mintingMaterialAsync(material, materialMintingRequestDto);

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


    @Async
    public void mintingMaterialAsync(Material material, MaterialMintingRequestDto materialMintingRequestDto) {
        // CID 값, 메타데이터 JSON -> IPFS 업로드 후 CID 값 받아오기
        LocalDateTime now = LocalDateTime.now();
        Mono<String> metadataCID = pinataService.pinJsonToIPFS(material, materialMintingRequestDto.getTitle(), material.getSource(), now, PINATA_JWT);
        metadataCID.subscribe(cid -> {
            log.info("metadataCID : {}", cid);
            // 받아온 CID 값으로 MINTING 진행
            CompletableFuture<TransactionReceipt> transactionReceiptCompletableFuture = nft.mintNFT(WALLET_ADDRESS, "ipfs://" + cid + "/").sendAsync();
            transactionReceiptCompletableFuture.thenAccept(transactionReceipt -> {
                materialAsyncService.publishEvent(material.getId(), transactionReceipt.getTransactionHash());
            }).exceptionally(ex -> {
                log.error("Error during minting NFT: {}", ex.getMessage());
                throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("NFT minting에 실패했습니다. : " + ex.getMessage()).build();
            });
        }, error -> {
            log.error("Error during pinning JSON to IPFS: {}", error.getMessage());
            throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("IPFS 업로드를 실패했습니다. : " + error).build();
        });
    }

}
