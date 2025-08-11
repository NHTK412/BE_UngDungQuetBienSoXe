package com.example.licenseplate.service;


import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FcmService {

    /**
     * Gửi notification đơn lẻ tới thiết bị có FCM token
     *
     * @param token FCM token của thiết bị nhận
     * @param title Tiêu đề thông báo
     * @param body Nội dung thông báo
     */
    public void sendNotification(String token, String title, String body) {
        Notification notification = Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build();

        Message message = Message.builder()
                .setToken(token)               // gửi đến thiết bị có token này
                .setNotification(notification) // thêm phần notification
                .build();

        try {
            String response = FirebaseMessaging.getInstance().send(message);
            System.out.println("Sent message: " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}