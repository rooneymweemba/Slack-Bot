package primebot.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class SlackMessageService {

        private static final Logger log = LoggerFactory.getLogger(SlackMessageService.class);
        @Value("${slack.token}")
        private String slackToken;

        private final RestTemplate restTemplate = new RestTemplate();
        private static final String SLACK_URL = "https://slack.com/api/chat.postMessage";

        public boolean sendMessage(String channel, String text) {
                log.info("Sending message to channel: {} with text {}", channel, text);
                try {
                        HttpHeaders headers = new HttpHeaders();
                        headers.setBearerAuth(slackToken);
                        headers.setContentType(MediaType.APPLICATION_JSON);

                        Map<String, String> payload = new HashMap<>();
                        payload.put("channel", channel);
                        payload.put("text", text);

                        HttpEntity<Map<String, String>> request = new HttpEntity<>(payload, headers);

                        restTemplate.postForEntity(SLACK_URL, request, String.class);
                        ResponseEntity<String> response = restTemplate.postForEntity(SLACK_URL, request, String.class);

                        log.info("Slack response status: {}", response.getStatusCode());
                        log.info("Slack response body: {}", response.getBody());
                        if (response.getStatusCode().is2xxSuccessful()) {
                                log.info("Message sent successfully to Slack channel: {}", channel);
                                return true;
                        } else {
                                log.error("Failed to send message to Slack channel: {}", channel);
                                return false;
                        }
                } catch (Exception e) {
                        log.error("Error sending message to Slack", e);
                        return false;
                }

        }
}
