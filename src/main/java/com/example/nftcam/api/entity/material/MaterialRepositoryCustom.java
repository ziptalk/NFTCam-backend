package com.example.nftcam.api.entity.material;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MaterialRepositoryCustom {
    List<Material> findAllByUserIdWithPaging(Long userId, Long cursorId, Pageable pageable);
}
