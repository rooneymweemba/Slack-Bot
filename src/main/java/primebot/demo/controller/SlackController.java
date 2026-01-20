package primebot.demo.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import primebot.demo.service.SlackMessageService;

@RestController
@RequestMapping("/slack")
public class SlackController {

    private final SlackMessageService slackClient;

    public SlackController(SlackMessageService slackClient) {
        this.slackClient = slackClient;
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }

    @PostMapping("/message")
    public ResponseEntity<Void> sendMessage(
            @RequestParam String channel,
            @RequestParam String text
    ) {
        slackClient.sendMessage(channel, text);
        return ResponseEntity.ok().build();
    }
}