package com.example.nftcam.api.entity.material;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MaterialRepository extends JpaRepository<Material, Long>, MaterialRepositoryCustom {
    List<Material> findAllByUserIdWithPaging(Long userId, Long cursorId, Pageable pageable);

    Optional<Material> findByIdAndUser_Id(Long materialId, Long userId);

    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Material m SET m.nftId = :nftId, m.isMinting = :state WHERE m.id = :materialId")
    void updateMaterialNftId(String nftId, Long materialId, MintState state);
}
