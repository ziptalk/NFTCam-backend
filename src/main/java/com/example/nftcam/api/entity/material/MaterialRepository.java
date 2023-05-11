package com.example.nftcam.api.entity.material;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Long>, MaterialRepositoryCustom {
    List<Material> findAllByUserIdWithPaging(Long userId, Long cursorId, Pageable pageable);

    Optional<Material> findByIdAndUser_Id(Long materialId, Long userId);
}
