//package com.foodygo.configuration;
//
//import com.google.auth.oauth2.GoogleCredentials;
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.FirebaseOptions;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.io.FileInputStream;
//import java.io.IOException;
//
////TEST ĐỂ UPLOAD FILE TỪ FIREBASE CÓ CONFIG SẴN Ở RESOURCES
//@Configuration
//public class QuanTest_FirebaseConfig {
//    @Bean
//    public FirebaseApp initializeFirebase() throws IOException {
//        FileInputStream serviceAccount =
//                new FileInputStream("src/main/resources/swp391-f046d-firebase-adminsdk-drdyq-e270ed7356.json");
//
//        FirebaseOptions options = FirebaseOptions.builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .setStorageBucket("swp391-f046d.appspot.com")
//                .build();
//
//        FirebaseApp firebaseApp;
//        if (FirebaseApp.getApps().isEmpty()) {
//            firebaseApp = FirebaseApp.initializeApp(options);
//        } else {
//            firebaseApp = FirebaseApp.getInstance();
//        }
//
//        return firebaseApp;
//    }
//}
