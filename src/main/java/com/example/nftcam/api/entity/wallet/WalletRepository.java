package com.example.nftcam.api.entity.wallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    // Create
    Optional<Wallet> findByIdAndUser_Id(Long walletId, Long userId);

    // Read
    List<Wallet> findAllByUser_IdOrderByUpdatedAtDesc(Long userId);
    boolean existsByWalletAddressAndUser_Id(String walletAddress, Long userId);

    // Update
    @Modifying(clearAutomatically = true)
    @Query(value = "UPDATE Wallet w SET w.walletName = :walletName, w.walletAddress = :walletAddress, w.updatedAt = :now WHERE w.id = :walletId")
    void updateWalletInfo(String walletName, String walletAddress, LocalDateTime now, Long walletId);

    // Delete
    void deleteById(Long walletId);
}
