package com.example.nftcam.api.service.material;

import com.example.nftcam.api.dto.material.request.MaterialSaveRequestDto;
import com.example.nftcam.api.entity.material.MaterialRepository;
import com.example.nftcam.api.entity.user.UserRepository;
import com.example.nftcam.api.entity.user.details.UserAccount;
import com.example.nftcam.utils.LocationConvertion;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaterialService {
    private final UserRepository userRepository;
    private final MaterialRepository materialRepository;
    private final LocationConvertion locationConvertion;

    @Transactional
    public void saveMaterial(UserAccount userAccount, MaterialSaveRequestDto materialSaveRequestDto) {

    }

    public void test(){
        // 경도, 위도로 입력
        locationConvertion.coordToAddr("37.496496","127.029320");
    }
}
