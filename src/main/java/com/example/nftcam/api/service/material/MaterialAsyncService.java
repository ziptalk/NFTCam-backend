package com.example.nftcam.api.service.material;

import com.example.nftcam.event.MintingCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialAsyncService {
    private final ApplicationEventPublisher eventPublisher;

    public void publishEvent(Long materialId, String hash) {
//        log.info("async publishEvent hash : {}", hash);
        eventPublisher.publishEvent(new MintingCompletedEvent(hash, materialId));
    }
}
