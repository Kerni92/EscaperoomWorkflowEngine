package de.kernebeck.escaperoom.escaperoomgame.webapp.pages;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.GameModel;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.IModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class GamePage extends WebPage {

    @SpringBean
    private GameService gameService;

    private IModel<Game> game = null;

    public GamePage() {
        super();
        add(new Label("test", "Es wurde kein Spiel übergeben"));
    }

    public GamePage(PageParameters pageParameters) {
        super(pageParameters);
        final String gameId = pageParameters.get("gameId") != null ? pageParameters.get("gameId").toString() : null;
        if (gameId != null) {
            game = new GameModel(gameId);
        }

        add(new Label("test", "Folgendes Spiel wird geladen: " + (game != null && game.getObject() != null ? game.getObject().getGameId() : " Es wurde keins Übergeben")));
    }


}
