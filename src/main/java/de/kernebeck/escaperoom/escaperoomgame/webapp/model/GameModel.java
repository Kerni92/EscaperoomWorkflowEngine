package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class GameModel extends AbstractEscaperoomLoadableDetachableModel<Game> {

    @SpringBean
    private transient GameService gameService;

    private String gameId;

    public GameModel() {
        //required for serialization and deserialization
        Injector.get().inject(this);
    }

    public GameModel(String gameId) {
        super();
        Injector.get().inject(this);
        this.gameId = gameId;
    }

    @Override
    protected Game loadInternal() {
        return gameService.findByGameId(gameId);
    }
}
