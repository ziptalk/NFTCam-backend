package com.example.nftcam.utils;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.example.nftcam.config.AwsS3Config;
import com.example.nftcam.exception.custom.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AmazonS3Uploader {
    private final AwsS3Config awsS3Config;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${image.path}")
    private String imagePath;

    public String saveFileAndGetUrl(MultipartFile multipartFile) throws Exception {
        String s3FileName = imagePath + UUID.randomUUID() + "_" + multipartFile.getOriginalFilename();

        ObjectMetadata objMeta = new ObjectMetadata();
        objMeta.setContentType(multipartFile.getContentType());
        objMeta.setContentLength(multipartFile.getSize());

        awsS3Config.amazonS3Client().putObject(new PutObjectRequest(bucket, s3FileName, multipartFile.getInputStream(), objMeta)
                .withCannedAcl(CannedAccessControlList.PublicRead));

        return awsS3Config.amazonS3Client().getUrl(bucket, s3FileName).toString();
    }

    public void deleteFile(String uuidFileName) {
        try {
            String keyName = imagePath + uuidFileName;
            boolean isObjectExist = awsS3Config.amazonS3Client().doesObjectExist(bucket, keyName);
            if (isObjectExist) {
                awsS3Config.amazonS3Client().deleteObject(bucket, keyName);
            }
        } catch (Exception e) {
            log.debug("Delete File failed", e);
        }
    }

    public File saveS3ObjectToFile(String imageUrl) {
        String keyword = "image";
        int index = imageUrl.indexOf(keyword);
        String key = imageUrl.substring(index);
        String decodedKey = null;
        try {
            decodedKey = URLDecoder.decode(key, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw CustomException.builder().httpStatus(HttpStatus.BAD_REQUEST).message("파일 이름 디코딩에 실패했습니다.").build();
        }
        log.info("decodedKey: {}", decodedKey);
        S3Object s3Object = awsS3Config.amazonS3Client().getObject(bucket, decodedKey);
        S3ObjectInputStream s3is = s3Object.getObjectContent();

        File tempFile;
        try {
            tempFile = File.createTempFile("temp-file-name", ".jpg");
            OutputStream os = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            // read from is to buffer
            while ((bytesRead = s3is.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            s3is.close();
            os.close();
        } catch (Exception e) {
            throw CustomException.builder().httpStatus(HttpStatus.INTERNAL_SERVER_ERROR).message(e.getMessage()).build();
        }
        return tempFile;
    }
}
