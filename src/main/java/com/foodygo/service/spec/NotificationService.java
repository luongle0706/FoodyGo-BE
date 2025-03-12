package com.foodygo.service.spec;

import com.foodygo.entity.Order;

public interface NotificationService {
    void sendNotification(Integer userId, String title, String body, String clickAction);
    void sendOrderStatusChangeUpdates(Order order);
}