package de.kernebeck.escaperoom.escaperoomgame;

import com.google.common.eventbus.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EscaperoomGameConfiguration {

    @Bean
    public EventBus eventBus() {
        return new EventBus();
    }


}
