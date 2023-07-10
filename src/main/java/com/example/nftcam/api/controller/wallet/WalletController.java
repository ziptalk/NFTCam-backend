package com.example.nftcam.api.controller.wallet;

import com.example.nftcam.api.dto.wallet.request.WalletInfoCreateRequestDto;
import com.example.nftcam.api.dto.wallet.request.WalletInfoUpdateRequestDto;
import com.example.nftcam.api.dto.wallet.response.WalletInfoResponseDto;
import com.example.nftcam.api.entity.user.details.UserAccount;
import com.example.nftcam.api.service.wallet.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Slf4j
@RequestMapping("/api/wallet")
@RequiredArgsConstructor
@RestController
public class WalletController {
    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<Map<String, List<WalletInfoResponseDto>>> getMyWalletInfoList
            (
                    @AuthenticationPrincipal UserAccount userAccount
            )
    {
        List<WalletInfoResponseDto> myWalletInfoList = walletService.getMyWalletInfoList(userAccount);
        Map<String, List<WalletInfoResponseDto>> response = Map.of("data", myWalletInfoList);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping
    public ResponseEntity<WalletInfoResponseDto> createNewMyWalletInfo
            (
                    @AuthenticationPrincipal UserAccount userAccount,
                    @RequestBody WalletInfoCreateRequestDto walletInfoCreateRequestDto
            )
    {
        WalletInfoResponseDto walletInfoResponseDto = walletService.createWalletInfo(userAccount, walletInfoCreateRequestDto);
        return ResponseEntity.ok().body(walletInfoResponseDto);
    }

    @PatchMapping("/{walletId}")
    public ResponseEntity<WalletInfoResponseDto> updateMyWalletInfo
            (
                    @AuthenticationPrincipal UserAccount userAccount,
                    @PathVariable Long walletId,
                    @RequestBody WalletInfoUpdateRequestDto walletInfoUpdateRequestDto
            )
    {
        WalletInfoResponseDto walletInfoResponseDto = walletService.updateMyWalletInfo(userAccount, walletInfoUpdateRequestDto, walletId);
        return ResponseEntity.ok().body(walletInfoResponseDto);
    }

    @DeleteMapping("/{walletId}")
    public ResponseEntity<Void> deleteMyWalletInfo
            (
                    @AuthenticationPrincipal UserAccount userAccount,
                    @PathVariable Long walletId
            )
    {
        walletService.deleteMyWalletInfo(userAccount, walletId);
        return ResponseEntity.ok().build();
    }
}
