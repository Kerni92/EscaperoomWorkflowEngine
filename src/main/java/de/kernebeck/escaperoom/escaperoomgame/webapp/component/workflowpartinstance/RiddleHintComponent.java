package de.kernebeck.escaperoom.escaperoomgame.webapp.component.workflowpartinstance;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.RiddleHintModel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.GenericPanel;

public class RiddleHintComponent extends GenericPanel<RiddleHint> {

    public RiddleHintComponent(String id, RiddleHintModel riddleHintModel) {
        super(id, riddleHintModel);

        final Label hintText = new Label("hintText", getModelObject().getContent());
        hintText.setOutputMarkupId(true);
        hintText.setEscapeModelStrings(false);
        add(hintText);
    }
}
