package de.kernebeck.escaperoom.escaperoomgame.webapp.component.gameresult;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public class GameResultComponent extends GenericPanel<Game> {

    public GameResultComponent(String id, IModel<Game> model) {
        super(id, model);

        add(new Label("placeholder", "Game finished"));
    }
}
