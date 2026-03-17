package io.hexlet.blog.component;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Component
@ConfigurationProperties(prefix = "app")
@Setter
@Getter
public class DefaultAppProperties {
    private String welcomeMessage;
    private String adminEmail;
}
