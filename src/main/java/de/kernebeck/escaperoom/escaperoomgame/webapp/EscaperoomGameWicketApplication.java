package de.kernebeck.escaperoom.escaperoomgame.webapp;

import com.google.common.eventbus.EventBus;
import de.kernebeck.escaperoom.escaperoomgame.core.service.authorisation.UserService;
import de.kernebeck.escaperoom.escaperoomgame.webapp.pages.GamePage;
import de.kernebeck.escaperoom.escaperoomgame.webapp.pages.HomePage;
import de.kernebeck.escaperoom.escaperoomgame.webapp.service.WebSocketEventService;
import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.resource.loader.IStringResourceLoader;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class EscaperoomGameWicketApplication extends WebApplication {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private UserService userService;

    @Autowired
    private EventBus eventBus;

    @Override
    protected void init() {
        super.init();

        this.getCspSettings().blocking().disabled();

        List<IStringResourceLoader> resourceLoaders = getResourceSettings().
                getStringResourceLoaders();
        resourceLoaders.add(0, new IStringResourceLoader() {
            @Override
            public String loadStringResource(Class<?> clazz, String key, Locale locale, String style, String variation) {
                return loadResource(key, locale);
            }

            @Override
            public String loadStringResource(Component component, String key, Locale locale, String style, String variation) {
                return loadResource(key, locale);
            }

            private String loadResource(String key, Locale pLocale) {
                Locale locale = pLocale;
                if (locale == null) {
                    locale = Locale.getDefault();
                }
                final ResourceBundle b = ResourceBundle.getBundle("localization.EscaperoomgameApplication", locale);
                if (b != null && b.containsKey(key)) {
                    return b.getString(key);
                }
                return key;
            }
        });

        this.getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext, true));

        this.mountPage("/game", GamePage.class);

        eventBus.register(WebSocketEventService.getInstance());

        //create and log default user if not existing
        if (!userService.existsUser("admin")) {
            userService.createUser("admin", "admin", "admin", "testpassword", true);
        }
    }


    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

}
