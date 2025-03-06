package com.foodygo.service.impl;

import com.foodygo.configuration.S3Properties;
import com.foodygo.exception.FileSizeLimitExceededException;
import com.foodygo.service.spec.S3Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements S3Service {
    private final S3Client s3Client;
    private final S3Properties s3Properties;

    private int MAX_FILE_SIZE = 10_485_760;

    @Override
    public String uploadFileToS3(MultipartFile file, String folderName) {
        if(file.getSize() > MAX_FILE_SIZE) {
            throw new FileSizeLimitExceededException("Maximum file size is " + MAX_FILE_SIZE);
        }
        File fileObj = convertMultipartFileToFile(file);
        return uploadFileToS3(fileObj, folderName);
    }

    @Override
    public String uploadFileToS3(File fileObj, String folderName) {
        String fileName = System.currentTimeMillis() + "_" + fileObj.getName();

        String key = folderName + "/" + fileName;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(key)
                    .build();

            s3Client.putObject(putObjectRequest, fileObj.toPath());

            return "https://" + s3Properties.getBucketName() + ".s3.amazonaws.com/" + key;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        } finally {
            fileObj.delete();
        }
    }
    private File convertMultipartFileToFile(MultipartFile file) {
        File convertedFile = null;
        try {
            // Create a temporary file
            convertedFile = File.createTempFile("temp", file.getOriginalFilename());
            try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
                fos.write(file.getBytes());
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return convertedFile;
    }

    @Override
    public InputStream downloadFileAsInputStream(String s3Url) {
        // Tách bucket và key từ S3 URL
        URI uri = URI.create(s3Url);
        String bucketName = s3Properties.getBucketName();
        String key = uri.getPath().substring(1);

        // Tải file từ S3
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(key)
                .build();

        return s3Client.getObject(getObjectRequest);
    }

    @Override
    public String getFileNameFromS3Url(String s3Url) {
        URI uri = URI.create(s3Url);
        String path = uri.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }
}
