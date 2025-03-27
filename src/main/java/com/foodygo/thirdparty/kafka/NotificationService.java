package com.foodygo.thirdparty.kafka;

import com.foodygo.entity.Order;

public interface NotificationService {
    void requestMessage(Integer userId, String title, String body, String clickAction);
    void sendOrderStatusChangeUpdates(Order order);
}