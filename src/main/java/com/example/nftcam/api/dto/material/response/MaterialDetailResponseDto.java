package com.example.nftcam.api.dto.material.response;

import com.example.nftcam.api.entity.material.ChainType;
import com.example.nftcam.api.entity.material.Material;
import com.example.nftcam.api.entity.material.MintState;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MaterialDetailResponseDto {
private Long materialId;
    private String title;
    private String source;
    private String nftId;
    private MintState isMinting;
    private ChainType chainType;
    private String device;
    private String address;
    private String date;

    @Builder
    public MaterialDetailResponseDto(Long materialId, String title, String source, String nftId, MintState isMinting, ChainType chainType, String device, String address, String date) {
        this.materialId = materialId;
        this.title = title;
        this.source = source;
        this.nftId = nftId;
        this.isMinting = isMinting;
        this.chainType = chainType;
        this.device = device;
        this.address = address;
        this.date = date;
    }

    public static MaterialDetailResponseDto of(Material material){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.MM.dd a hh:mm");
        return MaterialDetailResponseDto.builder()
                .materialId(material.getId())
                .title(material.getTitle())
                .source(material.getSource())
                .nftId(Optional.ofNullable(material.getNftId()).orElseGet(() -> "미발행"))
                .isMinting(material.getIsMinting())
                .chainType(material.getChainType() == null ? null : material.getChainType())
                .device(material.getDevice())
                .address(material.getAddress())
                .date(material.getTakenAt().format(formatter))
                .build();
    }
}
