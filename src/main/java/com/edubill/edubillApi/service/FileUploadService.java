package com.edubill.edubillApi.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.edubill.edubillApi.config.S3Config;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileUploadService {

    private final S3Config s3Config;

    public String saveImageFile(MultipartFile file) throws IOException {
        //uploadPath 생성
        String originalFilename = file.getOriginalFilename();
        String fileExtension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
        String uploadFilename = UUID.randomUUID() + "." + fileExtension;

        log.info("File upload started: " + uploadFilename);

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());

        AmazonS3Client amazonS3Client = s3Config.amazonS3Client();
        String bucketName = s3Config.getBucketName();

        amazonS3Client.putObject(
                new PutObjectRequest(bucketName, uploadFilename, file.getInputStream(), objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead)
        );
        //ex) https://edubill-prd.s3.ap-northeast-2.amazonaws.com/<uploadPath>
        return amazonS3Client.getUrl(bucketName, uploadFilename).toString();
    }
}
