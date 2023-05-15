package com.example.nftcam.api.service.auth;

import com.example.nftcam.api.dto.auth.request.LoginRequestDto;
import com.example.nftcam.api.dto.auth.response.LoginResponseDto;
import com.example.nftcam.api.entity.token.UserToken;
import com.example.nftcam.api.entity.token.UserTokenRepository;
import com.example.nftcam.api.entity.user.User;
import com.example.nftcam.api.entity.user.UserRepository;
import com.example.nftcam.filter.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final UserTokenRepository userTokenRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @Transactional
    public LoginResponseDto loginAndRegister(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUuid(loginRequestDto.getUuid())
                            .orElseGet(() -> userRepository.save(loginRequestDto.toEntity()));

        String accessToken = jwtTokenUtil.generateAccessToken(user.getId(), user.getRole(), user.getUuid(), true);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId(), user.getRole(), user.getUuid(), true);
        userTokenRepository.findByUserId(user.getId())
                .ifPresentOrElse(
                        userToken -> userToken.updateRefreshToken(refreshToken),
                        () -> userTokenRepository.save(UserToken.builder().user(user).refreshToken(refreshToken).build())
                );
        return LoginResponseDto.of(accessToken, refreshToken);
    }

//    @Transactional
//    public LoginResponseDto reissue(LoginRequestDto loginRequestDto) {
//
//    }
}
