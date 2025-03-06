package com.foodygo.service.spec;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;

public interface S3Service {
    String uploadFileToS3(File fileObj, String folderName);
    String uploadFileToS3(MultipartFile file, String folderName);
    InputStream downloadFileAsInputStream(String s3Url);
    String getFileNameFromS3Url(String s3Url);
}
