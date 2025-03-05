//package com.foodygo.controller.test;
//
//import com.foodygo.dto.response.ObjectResponse;
//import com.foodygo.service.spec.NotificationService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/authentications")
//public class NotificationController {
//
//    private final NotificationService notificationService;
//
//    @GetMapping("/send-notification")
//    public ResponseEntity<?> sendStuff(
//            Integer userId,
//            String title,
//            String body,
//            String clickAction
//    ) {
//        notificationService.sendNotification(userId, title, body, clickAction);
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(
//                        ObjectResponse.builder()
//                                .status(HttpStatus.OK.toString())
//                                .message("DSKDMSKDMSD")
//                                .data(null)
//                                .build()
//                );
//    }
//
//}
