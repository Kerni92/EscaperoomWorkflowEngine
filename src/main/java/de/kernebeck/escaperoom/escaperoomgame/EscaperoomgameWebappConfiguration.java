package de.kernebeck.escaperoom.escaperoomgame;

import de.kernebeck.escaperoom.escaperoomgame.webapp.EscaperoomGameWicketApplication;
import org.apache.wicket.protocol.http.WicketFilter;
import org.apache.wicket.protocol.ws.javax.JavaxWebSocketFilter;
import org.apache.wicket.protocol.ws.javax.WicketServerEndpointConfig;
import org.apache.wicket.spring.SpringWebApplicationFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

@Configuration
public class EscaperoomgameWebappConfiguration {

    @Bean
    public EscaperoomGameWicketApplication wicketApplication() {
        return new EscaperoomGameWicketApplication();
    }

    @Bean
    public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
    }

    @Bean
    public ServletContextInitializer wicketFilterServletContextInitializer() {
        return servletContext -> {
            final FilterRegistration.Dynamic filter = servletContext.addFilter("wicket-filter", JavaxWebSocketFilter.class);

            filter.setInitParameter(WicketFilter.APP_FACT_PARAM, SpringWebApplicationFactory.class.getName());
            filter.setInitParameter(WicketFilter.IGNORE_PATHS_PARAM, "/api/*");
            filter.setInitParameter("applicationBean", "wicketApplication");
            filter.setInitParameter("configuration", "development");
            filter.setInitParameter(WicketFilter.FILTER_MAPPING_PARAM, "/*");
            filter.setAsyncSupported(true);
            filter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), false, "/*");
        };
    }

    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }

    @Bean
    public WicketServerEndpointConfig wicketServerEndpointConfig() {
        return new WicketServerEndpointConfig();
    }
}
