package com.foodygo.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp authFirebaseApp() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            InputStream inputStream = new ClassPathResource("theanh-firebase.json").getInputStream();

            FirebaseOptions firebaseOptions = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(inputStream))
                    .build();

            return FirebaseApp.initializeApp(firebaseOptions, "firebase-authorize");
        }
        return FirebaseApp.getInstance("firebase-authorize");
    }

    @Bean
    public FirebaseAuth authFirebaseAuth() throws IOException {
        return FirebaseAuth.getInstance(authFirebaseApp());
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        return FirebaseMessaging.getInstance(authFirebaseApp());
    }

}
