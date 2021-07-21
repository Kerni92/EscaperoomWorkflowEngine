package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleInstanceService;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class RiddleInstanceModel extends LoadableDetachableModel<RiddleInstance> {

    @SpringBean
    private RiddleInstanceService riddleInstanceService;

    private Long id;

    public RiddleInstanceModel() {
        //required for serialization and deserialization
        Injector.get().inject(this);
    }

    public RiddleInstanceModel(Long id) {
        this.id = id;
        Injector.get().inject(this);
    }

    @Override
    protected RiddleInstance load() {
        if (id != null) {
            return riddleInstanceService.findById(id);
        }
        return null;
    }
}
