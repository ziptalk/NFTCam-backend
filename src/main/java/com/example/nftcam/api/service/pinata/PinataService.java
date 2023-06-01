package com.example.nftcam.api.service.pinata;

import com.example.nftcam.api.dto.util.IPFSResponseDto;
import com.example.nftcam.api.entity.material.Material;
import com.example.nftcam.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PinataService {

    public String pinFileToIPFS(String title, File file, String jwt) {
        WebClient webClient = WebClient.builder().baseUrl("https://api.pinata.cloud").build();

        MultipartBodyBuilder bodyBuilder = new MultipartBodyBuilder();
        bodyBuilder.part("file", new FileSystemResource(file), MediaType.APPLICATION_OCTET_STREAM);
        bodyBuilder.part("pinataMetadata", String.format("{\"name\": \"%s\"}", title));
        MultiValueMap<String, HttpEntity<?>> multipartBody = bodyBuilder.build();

        IPFSResponseDto response = webClient.post()
                .uri("/pinning/pinFileToIPFS")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + jwt)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .bodyValue(multipartBody)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(IPFSResponseDto.class)
                .timeout(Duration.ofMinutes(5L))
                .blockOptional().orElseThrow(
                        () -> CustomException.builder()
                                .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                                .message("Pinata pinFileToIPFS error")
                                .build()
                );

        return response.getIpfsHash();
    }

    public Mono<String> pinJsonToIPFS(Material material, String title, String imageUrl, LocalDateTime now, String jwt) {
        WebClient webClient = WebClient.create("https://api.pinata.cloud");

        Map<String, Object> pinataMetadata = new HashMap<>();
        pinataMetadata.put("name", title+"-metadata");

        Map<String, String> pinataContent = new HashMap<>();
        pinataContent.put("name", title);
        pinataContent.put("description", title+"-description");
        pinataContent.put("image", imageUrl);
        pinataContent.put("time", String.valueOf(now));
        pinataContent.put("device", material.getDevice());
        pinataContent.put("location", material.getAddress());

        Map<String, Object> body = new HashMap<>();
        body.put("pinataMetadata", pinataMetadata);
        body.put("pinataContent", pinataContent);

        return webClient.post()
                .uri("/pinning/pinJSONToIPFS")
                .header("Authorization", "Bearer " + jwt)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(IPFSResponseDto.class)
                .timeout(Duration.ofMinutes(5L))
                .map(IPFSResponseDto::getIpfsHash);
    }

    public File convertMultipartFileToFile(MultipartFile multipartFile) throws IOException {
        Path tempDir = Files.createTempDirectory("nftcamTemp");
        File tempFile = tempDir.resolve(multipartFile.getOriginalFilename()).toFile();
        multipartFile.transferTo(tempFile);
        return tempFile;
    }

    public void deleteFile(File file) {
        boolean deleted = file.delete();
        if (!deleted) {
            throw CustomException.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message("Failed to delete").build();
        }
    }
}
