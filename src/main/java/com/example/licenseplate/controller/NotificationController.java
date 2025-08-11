package com.example.licenseplate.controller;
import org.springframework.web.bind.annotation.*;
import com.example.licenseplate.service.FcmService;

@RestController
@RequestMapping("/api/fcm")
public class NotificationController {

    private final FcmService fcmService;

    public NotificationController(FcmService fcmService) {
        this.fcmService = fcmService;
    }

    /**
     * Gửi thông báo đến thiết bị có token truyền vào
     * POST /api/fcm/send
     * Body JSON:
     * {
     *   "token": "token_string",
     *   "title": "Tiêu đề",
     *   "body": "Nội dung"
     * }
     */
    @PostMapping("/send")
    public String sendNotification(@RequestBody NotificationRequest request) {
        fcmService.sendNotification(request.getToken(), request.getTitle(), request.getBody());
        return "Notification sent!";
    }

    public static class NotificationRequest {
        private String token;
        private String title;
        private String body;

        // getters, setters
        public String getToken() { return token; }
        public void setToken(String token) { this.token = token; }
        public String getTitle() { return title; }
        public void setTitle(String title) { this.title = title; }
        public String getBody() { return body; }
        public void setBody(String body) { this.body = body; }
    }
}
