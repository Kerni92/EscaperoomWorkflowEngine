package de.kernebeck.escaperoom.escaperoomgame;

import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.GameExecutionService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

@SpringBootApplication
public class EscaperoomgameTestApplication {

	public static void main(String[] args) {
		final SpringApplication application = new SpringApplication(EscaperoomgameTestApplication.class);
		application.addListeners((ApplicationListener<ContextClosedEvent>) event -> {
			//we have to stop running games on server shutdown to get a correct game timing
			final GameExecutionService gameExecutionService = event.getApplicationContext().getBean(GameExecutionService.class);
			gameExecutionService.stopRunningGames();
		});
		application.run(args);
	}

}
