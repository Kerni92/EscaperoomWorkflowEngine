package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleInstanceService;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RiddleInstanceModel extends AbstractEscaperoomLoadableDetachableModel<RiddleInstance> {

    @SpringBean
    private transient RiddleInstanceService riddleInstanceService;

    private Long id;

    public RiddleInstanceModel() {
        super();
        //required for serialization and deserialization
    }

    public RiddleInstanceModel(Long id) {
        super();
        this.id = id;
    }

    @Override
    protected RiddleInstance loadInternal() {
        if (id != null) {
            return riddleInstanceService.findById(id);
        }
        return null;
    }
}
