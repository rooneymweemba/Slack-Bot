package primebot.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.beans.factory.annotation.Value;
public class StackConfig {
    @Value("$slack.token")
    private String slackToken;

    @Bean
    public String slackToken() {
        return slackToken;
    }


}
