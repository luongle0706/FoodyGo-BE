package com.foodygo.service.spec;

public interface NotificationService {
    void sendNotification(Integer userId, String title, String body, String clickAction);
}