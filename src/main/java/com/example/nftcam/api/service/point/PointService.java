package com.example.nftcam.api.service.point;

import com.example.nftcam.api.dto.point.request.PointChargeRequestDto;
import com.example.nftcam.api.dto.point.response.PointChargeResponseDto;
import com.example.nftcam.api.entity.user.User;
import com.example.nftcam.api.entity.user.UserRepository;
import com.example.nftcam.api.entity.user.details.UserAccount;
import com.example.nftcam.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PointService {
    private final UserRepository userRepository;

    @Transactional
    public PointChargeResponseDto addPoint(UserAccount userAccount, PointChargeRequestDto pointChargeRequestDto) {
        User user = userRepository.findByUuid(userAccount.getUuid())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("해당 유저가 없습니다. uuid=" + userAccount.getUuid()).build());
        userRepository.addPoint(userAccount.getUserId(), pointChargeRequestDto.getPoint());
        return PointChargeResponseDto.builder()
                .point(user.getPoint() + pointChargeRequestDto.getPoint())
                .build();
    }

    public Map<String, Long> getMyPoint(UserAccount userAccount) {
        User user = userRepository.findByUuid(userAccount.getUuid())
                .orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("해당 유저가 없습니다. uuid=" + userAccount.getUuid()).build());
        return Map.of("point", user.getPoint());
    }
}
