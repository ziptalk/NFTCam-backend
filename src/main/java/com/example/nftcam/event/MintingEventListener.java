package com.example.nftcam.event;

import com.example.nftcam.api.entity.material.Material;
import com.example.nftcam.api.entity.material.MaterialRepository;
import com.example.nftcam.api.entity.material.MintState;
import com.example.nftcam.api.entity.user.UserRepository;
import com.example.nftcam.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
@RequiredArgsConstructor
public class MintingEventListener {
    private final MaterialRepository materialRepository;
    private final UserRepository userRepository;

    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void updateMaterialNFTId(MintingCompletedEvent event) {
        Long materialId = event.getMaterialId();
        Material material = materialRepository.findById(materialId)
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("존재하지 않는 material 입니다.").build());
//        log.info("listener transactionReceiptHash : {}", event.getTransactionHash());
//        log.info("listener blockNumber : {}", event.getBlockNum());
        userRepository.minusPoint(material.getUser().getId(),200L);
        materialRepository.updateMaterialNftId(event.getTransactionHash(), material.getId(), MintState.MINTED);
    }
}
