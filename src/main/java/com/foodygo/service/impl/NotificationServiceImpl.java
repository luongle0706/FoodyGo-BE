package com.foodygo.service.impl;

import com.foodygo.entity.FcmToken;
import com.foodygo.entity.Order;
import com.foodygo.entity.User;
import com.foodygo.enums.OrderStatus;
import com.foodygo.repository.FcmTokenRepository;
import com.foodygo.service.spec.NotificationService;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    private final FcmTokenRepository fcmTokenRepository;
    private final FirebaseMessaging firebaseMessaging;

    @Override
    @Transactional
    public void sendNotification(Integer userId, String title, String body, String clickAction) {
        List<FcmToken> devices = fcmTokenRepository.findByUserUserID(userId);

        System.out.println("Devices found: " + devices.size());

        for (FcmToken deviceToken : devices) {
            System.out.println("Sending notification to device token: " + deviceToken.getToken());
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

    @Override
    @Transactional
    public void sendOrderStatusChangeUpdates(Order order) {
        System.out.println("Order: " + order.getId());
        System.out.println("Order status: " + order.getStatus());
        switch (order.getStatus()) {
            case OrderStatus.ORDERED -> sendNotification(
                    order.getRestaurant().getOwner().getUserID(),
                    "Đơn hàng mới: " + order.getId(),
                    "Tổng giá: " + order.getTotalPrice() + "VND",
                    "sample"
            );
            case OrderStatus.RESTAURANT_ACCEPTED -> sendNotification(
                    order.getCustomer().getUser().getUserID(),
                    "Đơn hàng " + order.getId() + " đã được xác nhận!",
                    order.getRestaurant().getName() + " đã xác nhận đơn hàng của bạn!",
                    "sample"
            );
            case OrderStatus.SHIPPING -> sendNotification(
                    order.getCustomer().getUser().getUserID(),
                    "Đơn hàng của bạn đang được giao",
                    order.getRestaurant().getName() + " đã tiến hành giao món đến bạn!",
                    "sample"
            );
            case OrderStatus.HUB_ARRIVED -> sendNotification(
                    order.getCustomer().getUser().getUserID(),
                    "Món ăn của bạn đã đến Hub " + order.getHub().getName(),
                    "Món ăn tổng giá " + order.getTotalPrice() + "VND đã đến hub. Xin hãy giành ít phút để lấy đơn hàng của bạn",
                    "sample"
            );
            case OrderStatus.COMPLETED -> {
                sendNotification(
                        order.getRestaurant().getOwner().getUserID(),
                        "Đơn hàng hoàn tất",
                        "Số FoodyXu tương ứng " + order.getTotalPrice() + "VND sẽ được chuyển vào trong ví của bạn.",
                        "sample"
                );
                sendNotification(
                        order.getCustomer().getUser().getUserID(),
                        "Đơn hàng hoàn tất",
                        "Xin cảm ơn vì đã sử dụng ứng dụng FoodyGo",
                        "sample"
                );
            }
        }
    }


}
