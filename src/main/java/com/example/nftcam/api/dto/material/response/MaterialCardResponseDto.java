package com.example.nftcam.api.dto.material.response;

import com.example.nftcam.api.entity.material.ChainType;
import com.example.nftcam.api.entity.material.Material;
import com.example.nftcam.api.entity.material.MintState;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MaterialCardResponseDto {
    private Long materialId;
    private String source;
    private MintState isMinting;
    private ChainType chainType;
    private String date;
    private String device;
    private String address;

    @Builder
    public MaterialCardResponseDto(Long materialId, String source, MintState isMinting, ChainType chainType, String date, String device, String address) {
        this.materialId = materialId;
        this.source = source;
        this.isMinting = isMinting;
        this.chainType = chainType;
        this.date = date;
        this.device = device;
        this.address = address;
    }

    public static MaterialCardResponseDto of(Material material){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd a hh:mm");
        return MaterialCardResponseDto.builder()
                .materialId(material.getId())
                .source(material.getSource())
                .isMinting(material.getIsMinting())
                .chainType(material.getChainType() == null ? null : material.getChainType())
                .date(material.getTakenAt().format(formatter))
                .device(material.getDevice())
                .address(material.getAddress())
                .build();
    }
}
