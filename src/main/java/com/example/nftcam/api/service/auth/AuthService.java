package com.example.nftcam.api.service.auth;

import com.example.nftcam.api.dto.auth.request.IssueTokenRequestDto;
import com.example.nftcam.api.dto.auth.request.ReissueTokenRequestDto;
import com.example.nftcam.api.dto.auth.response.IssueTokenResponseDto;
import com.example.nftcam.api.entity.token.UserToken;
import com.example.nftcam.api.entity.token.UserTokenRepository;
import com.example.nftcam.api.entity.user.User;
import com.example.nftcam.api.entity.user.UserRepository;
import com.example.nftcam.exception.custom.ExpiredTokenException;
import com.example.nftcam.exception.custom.NotFoundException;
import com.example.nftcam.filter.jwt.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    public IssueTokenResponseDto loginAndRegister(IssueTokenRequestDto issueTokenRequestDto) {
        User user = userRepository.findByUuid(issueTokenRequestDto.getUuid())
                            .orElseGet(() -> userRepository.save(issueTokenRequestDto.toEntity()));

        String accessToken = jwtTokenUtil.generateAccessToken(user.getId(), user.getRole(), user.getUuid(), true);
        String refreshToken = jwtTokenUtil.generateRefreshToken(user.getId(), user.getRole(), user.getUuid(), true);
        userTokenRepository.findByUserId(user.getId())
                .ifPresentOrElse(
                        userToken -> userToken.updateRefreshToken(refreshToken),
                        () -> userTokenRepository.save(UserToken.builder().user(user).refreshToken(refreshToken).build())
                );
        return IssueTokenResponseDto.of(accessToken, refreshToken);
    }

    @Transactional
    public IssueTokenResponseDto reissue(ReissueTokenRequestDto reissueTokenRequestDto) {
        UserToken userToken = userTokenRepository.findUserTokenByRefreshToken(reissueTokenRequestDto.getRefreshToken())
                .orElseThrow(() -> NotFoundException.builder().httpStatus(HttpStatus.NOT_FOUND).message("요청하신 토큰 정보를 찾을 수 없습니다.").build());

        if (jwtTokenUtil.isValidToken(reissueTokenRequestDto.getRefreshToken()) && jwtTokenUtil.isExpiredToken(reissueTokenRequestDto.getRefreshToken())) {
            String accessToken = jwtTokenUtil.generateAccessToken(userToken.getUser().getId(), userToken.getUser().getRole(), userToken.getUser().getUuid(), true);
            String refreshToken = jwtTokenUtil.generateRefreshToken(userToken.getUser().getId(), userToken.getUser().getRole(), userToken.getUser().getUuid(), true);
            userToken.updateRefreshToken(refreshToken);
            return IssueTokenResponseDto.of(accessToken, refreshToken);
        } else {
            throw ExpiredTokenException.builder().httpStatus(HttpStatus.UNAUTHORIZED).message("refresh 토큰이 만료되었습니다. 다시 로그인해주세요.").build();
        }
    }
}
