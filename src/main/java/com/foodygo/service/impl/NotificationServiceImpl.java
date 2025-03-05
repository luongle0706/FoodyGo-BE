package com.foodygo.service.impl;

import com.foodygo.entity.FcmToken;
import com.foodygo.repository.FcmTokenRepository;
import com.foodygo.service.spec.NotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final FcmTokenRepository fcmTokenRepository;
    private final FirebaseMessaging firebaseMessaging;

    @Override
    public void sendNotification(Integer userId, String title, String body, String clickAction) {
        List<FcmToken> devices = fcmTokenRepository.findByUserUserID(userId);

        for (FcmToken deviceToken : devices) {
            String fcmToken = deviceToken.getToken();
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .putData("title", title)
                    .putData("body", body)
                    .putData("clickAction", clickAction)
                    .build();
            try {
                firebaseMessaging.sendAsync(message).get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Unable to send to device with token {}", fcmToken, e);
            }
        }
    }
}
