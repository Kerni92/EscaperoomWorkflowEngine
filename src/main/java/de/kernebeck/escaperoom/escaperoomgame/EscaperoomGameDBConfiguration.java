package de.kernebeck.escaperoom.escaperoomgame;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EntityScan("de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity")
public class EscaperoomGameDBConfiguration {


}
