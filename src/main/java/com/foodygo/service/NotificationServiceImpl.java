package com.foodygo.service;

import com.foodygo.entity.FcmToken;
import com.foodygo.repository.FcmTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final FcmTokenRepository fcmTokenRepository;
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendNotification(Integer userId, String title, String body, String clickAction) {
        List<String> devices = fcmTokenRepository.findByUserId(userId);
        List<String> results = devices.stream().map((token) -> {
            System.out.println("Sending to");
            System.out.println(token);
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .putData("click_action", clickAction)
                    .build();
            try {
                return firebaseMessaging.sendAsync(message).get();
            } catch (InterruptedException | ExecutionException e) {
                System.out.println(e.getMessage());
            }
            return "Failed";
        }).toList();
    }
}
