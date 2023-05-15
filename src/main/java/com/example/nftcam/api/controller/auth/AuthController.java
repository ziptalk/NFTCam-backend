package com.example.nftcam.api.controller.auth;

import com.example.nftcam.api.dto.auth.request.IssueTokenRequestDto;
import com.example.nftcam.api.dto.auth.request.ReissueTokenRequestDto;
import com.example.nftcam.api.dto.auth.response.IssueTokenResponseDto;
import com.example.nftcam.api.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<IssueTokenResponseDto> login(@RequestBody IssueTokenRequestDto issueTokenRequestDto) {
        IssueTokenResponseDto issueTokenResponseDto = authService.loginAndRegister(issueTokenRequestDto);
        return ResponseEntity.ok().body(issueTokenResponseDto);
    }

    @PutMapping("/reissue")
    public ResponseEntity<IssueTokenResponseDto> reissue(@RequestBody ReissueTokenRequestDto reissueTokenRequestDto) {
        IssueTokenResponseDto issueTokenResponseDto = authService.reissue(reissueTokenRequestDto);
        return ResponseEntity.ok().body(issueTokenResponseDto);
    }
}
