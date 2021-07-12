package de.kernebeck.escaperoom.escaperoomgame.webapp.pages;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.Model;

public class HomePage extends WebPage {

    public HomePage() {
        super();
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        add(new Label("Home", Model.of("Test")));
    }
}
