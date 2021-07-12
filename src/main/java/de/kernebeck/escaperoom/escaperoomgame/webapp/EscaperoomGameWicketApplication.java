package de.kernebeck.escaperoom.escaperoomgame.webapp;

import de.kernebeck.escaperoom.escaperoomgame.webapp.pages.HomePage;
import org.apache.wicket.Page;
import org.apache.wicket.protocol.http.WebApplication;

public class EscaperoomGameWicketApplication extends WebApplication {

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public Class<? extends Page> getHomePage() {
        return HomePage.class;
    }

}
