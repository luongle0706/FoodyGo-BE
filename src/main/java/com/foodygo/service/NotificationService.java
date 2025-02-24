package com.foodygo.service;

public interface NotificationService {
    void sendNotification(Integer userId, String title, String body, String clickAction);
}