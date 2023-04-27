package com.example.nftcam.api.entity.material;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Long>, MaterialRepositoryCustom {
    List<Material> findAllByUserIdWithPaging(Long userId, Long cursorId, Pageable pageable);
}
