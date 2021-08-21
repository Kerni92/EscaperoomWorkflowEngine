package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.RiddleHint;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleHintService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RiddleHintModel extends AbstractEscaperoomLoadableDetachableModel<RiddleHint> {

    @SpringBean
    private transient RiddleHintService riddleHintService;

    private Long riddleHintId;

    public RiddleHintModel(RiddleHint object) {
        super(object);
        if (object != null) {
            this.riddleHintId = object.getId();
        }
    }

    public RiddleHintModel(Long riddleHintId) {
        Injector.get().inject(this);
        this.riddleHintId = riddleHintId;
    }

    @Override
    protected RiddleHint loadInternal() {
        if (riddleHintId != null) {
            return riddleHintService.load(riddleHintId);
        }
        return null;
    }
}
