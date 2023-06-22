package com.example.nftcam.api.entity.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // Create

    // Read
    Optional<User> findByUuid(String uuid);

    // Update
    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.point = u.point + :point WHERE u.id = :userId")
    void addPoint(Long userId, Long point);

    @Modifying(clearAutomatically = true)
    @Query("UPDATE User u SET u.point = u.point - :point WHERE u.id = :userId")
    void minusPoint(Long userId, Long point);

    // Delete
}
