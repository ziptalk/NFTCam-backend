package com.example.nftcam.api.controller.auth;

import com.example.nftcam.api.dto.auth.request.LoginRequestDto;
import com.example.nftcam.api.dto.auth.response.LoginResponseDto;
import com.example.nftcam.api.service.auth.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto loginResponseDto = authService.loginAndRegister(loginRequestDto);
        return ResponseEntity.ok().body(loginResponseDto);
    }

//    @PutMapping("/reissue")
//    public ResponseEntity<LoginResponseDto> reissue(@RequestBody LoginRequestDto loginRequestDto) {
//        LoginResponseDto loginResponseDto = authService.reissue(loginRequestDto);
//        return ResponseEntity.ok().body(loginResponseDto);
//    }
}
