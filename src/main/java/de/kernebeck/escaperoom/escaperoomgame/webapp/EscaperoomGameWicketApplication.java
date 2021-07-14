package de.kernebeck.escaperoom.escaperoomgame.webapp;

import de.kernebeck.escaperoom.escaperoomgame.webapp.pages.GamePage;
import de.kernebeck.escaperoom.escaperoomgame.webapp.pages.HomePage;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

public class EscaperoomGameWicketApplication extends WebApplication {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    protected void init() {
        super.init();

        this.getCspSettings().blocking().disabled();

        this.getComponentInstantiationListeners().add(new SpringComponentInjector(this, applicationContext));

        this.mountPage("/game", GamePage.class);

    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

}
