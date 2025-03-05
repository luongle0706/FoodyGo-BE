//package com.foodygo.controller.test;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.nio.file.StandardCopyOption;
//
//@RestController
//@RequestMapping("/api/v1/images")
//@RequiredArgsConstructor
//public class ImageController {
//
//    @Value("${image.saveLocation}")
//    private String uploadDir;
//
//    @Value("${image.getLocation}")
//    private String getLocation;
//
//    @PostMapping
//    public ResponseEntity<String> uploadImage(
//            @RequestParam("file") MultipartFile file
//    ) {
//        try {
//            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
//            Path filePath = Paths.get(uploadDir, fileName);
//            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//
//            String fileUrl = getLocation + fileName;
//            return ResponseEntity.ok(fileUrl);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//}
