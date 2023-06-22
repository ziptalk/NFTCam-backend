package com.example.nftcam.api.service.wallet;

import com.example.nftcam.api.dto.wallet.request.WalletInfoCreateRequestDto;
import com.example.nftcam.api.dto.wallet.request.WalletInfoUpdateRequestDto;
import com.example.nftcam.api.dto.wallet.response.WalletInfoResponseDto;
import com.example.nftcam.api.entity.user.UserRepository;
import com.example.nftcam.api.entity.user.details.UserAccount;
import com.example.nftcam.api.entity.wallet.Wallet;
import com.example.nftcam.api.entity.wallet.WalletRepository;
import com.example.nftcam.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public List<WalletInfoResponseDto> getMyWalletInfoList(UserAccount userAccount) {
        return walletRepository.findAllByUser_IdOrderByUpdatedAtDesc(userAccount.getUserId()).stream()
                .map(wallet -> WalletInfoResponseDto.of(wallet.getId(), wallet.getWalletName(), wallet.getWalletAddress()))
                .collect(Collectors.toList());
    }

    @Transactional
    public WalletInfoResponseDto createWalletInfo(UserAccount userAccount, WalletInfoCreateRequestDto walletInfoCreateRequestDto){
        Wallet wallet = walletRepository.save(Wallet.builder()
                .user(userRepository.findById(userAccount.getUserId()).orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("존재하지 않는 유저입니다.").build()))
                .walletName(walletInfoCreateRequestDto.getWalletName())
                .walletAddress(walletInfoCreateRequestDto.getWalletAddress())
                .build());
        return WalletInfoResponseDto.of(wallet.getId(), wallet.getWalletName(), wallet.getWalletAddress());
    }

    @Transactional
    public WalletInfoResponseDto updateMyWalletInfo(UserAccount userAccount, WalletInfoUpdateRequestDto walletInfoUpdateRequestDto, Long walletId) {
        Wallet wallet = walletRepository.findByIdAndUser_Id(walletId, userAccount.getUserId()).orElseThrow(() -> CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("존재하지 않는 정보 혹은 소유자가 아닙니다.").build());
        wallet.updateWalletInfo(walletInfoUpdateRequestDto.getWalletName(), walletInfoUpdateRequestDto.getWalletAddress());
        return WalletInfoResponseDto.of(wallet.getId(), wallet.getWalletName(), wallet.getWalletAddress());
    }

    @Transactional
    public void deleteMyWalletInfo(UserAccount userAccount, Long walletId) {
        walletRepository.findByIdAndUser_Id(walletId, userAccount.getUserId())
                .ifPresentOrElse(wallet -> walletRepository.deleteById(walletId), () -> {
                    throw CustomException.builder().httpStatus(HttpStatus.NOT_FOUND).message("존재하지 않는 정보 혹은 소유자가 아닙니다.").build();
                });
    }
}
