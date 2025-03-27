package com.foodygo.thirdparty.kafka;

import com.foodygo.entity.FcmToken;
import com.foodygo.entity.Order;
import com.foodygo.enums.OrderStatus;
import com.foodygo.repository.FcmTokenRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
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
    private final KafkaTemplate<String, NotificationRequestDTO> kafkaTemplate;

    @Transactional
    @KafkaListener(topics = "foodygo-topic", groupId = "foodygo-corp")
    public void sendNotification(NotificationRequestDTO request) {
        List<FcmToken> devices = fcmTokenRepository.findByUserUserID(request.getUserId());

        System.out.println("Devices found: " + devices.size());

        for (FcmToken deviceToken : devices) {
            System.out.println("Sending notification to device token: " + deviceToken.getToken());
            String fcmToken = deviceToken.getToken();
            Message message = Message.builder()
                    .setToken(fcmToken)
                    .putData("title", request.getTitle())
                    .putData("body", request.getBody())
                    .putData("clickAction", request.getClickAction())
                    .build();
            try {
                firebaseMessaging.sendAsync(message).get();
            } catch (InterruptedException | ExecutionException e) {
                log.error("Unable to send to device with token {}", fcmToken, e);
            }
        }
    }

    @Override
    public void requestMessage(Integer userId, String title, String body, String clickAction) {
        kafkaTemplate.send("foodygo-topic", NotificationRequestDTO.builder()
                .userId(userId)
                .title(title)
                .body(body)
                .clickAction(clickAction)
                .build());
    }

    @Override
    @Transactional
    public void sendOrderStatusChangeUpdates(Order order) {
        System.out.println("Order: " + order.getId());
        System.out.println("Order status: " + order.getStatus());
        switch (order.getStatus()) {
            case OrderStatus.ORDERED -> requestMessage(
                    order.getRestaurant().getOwner().getUserID(),
                    "Đơn hàng mới: " + order.getId(),
                    "Tổng giá: " + order.getTotalPrice() + "VND",
                    "sample"
            );
            case OrderStatus.RESTAURANT_ACCEPTED -> requestMessage(
                    order.getCustomer().getUser().getUserID(),
                    "Đơn hàng " + order.getId() + " đã được xác nhận!",
                    order.getRestaurant().getName() + " đã xác nhận đơn hàng của bạn!",
                    "sample"
            );
            case OrderStatus.SHIPPING -> requestMessage(
                    order.getCustomer().getUser().getUserID(),
                    "Đơn hàng của bạn đang được giao",
                    order.getRestaurant().getName() + " đã tiến hành giao món đến bạn!",
                    "sample"
            );
            case OrderStatus.HUB_ARRIVED -> requestMessage(
                    order.getCustomer().getUser().getUserID(),
                    "Món ăn của bạn đã đến Hub " + order.getHub().getName(),
                    "Món ăn tổng giá " + order.getTotalPrice() + "VND đã đến hub. Xin hãy giành ít phút để lấy đơn hàng của bạn",
                    "sample"
            );
            case OrderStatus.COMPLETED -> {
                requestMessage(
                        order.getRestaurant().getOwner().getUserID(),
                        "Đơn hàng hoàn tất",
                        "Số FoodyXu tương ứng " + order.getTotalPrice() + "VND sẽ được chuyển vào trong ví của bạn.",
                        "sample"
                );
                requestMessage(
                        order.getCustomer().getUser().getUserID(),
                        "Đơn hàng hoàn tất",
                        "Xin cảm ơn vì đã sử dụng ứng dụng FoodyGo",
                        "sample"
                );
            }
        }
    }


}
