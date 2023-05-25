package com.example.nftcam.api.controller.WEB3;

import com.example.nftcam.api.entity.user.details.UserAccount;
import com.example.nftcam.api.service.WEB3.Web3jService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/web3")
@RequiredArgsConstructor
@RestController
public class MintController {
    private final Web3jService web3jService;

    @GetMapping("/mint")
    public ResponseEntity<?> minting(@AuthenticationPrincipal UserAccount userAccount) throws Exception {
        web3jService.getContractName();
        web3jService.nftCreate();
        return ResponseEntity.ok().body("Minting Success");
    }

}
