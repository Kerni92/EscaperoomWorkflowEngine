package de.kernebeck.escaperoom.escaperoomgame.webapp.model;

import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.LoadableDetachableModel;

public abstract class AbstractEscaperoomLoadableDetachableModel<T> extends LoadableDetachableModel<T> {

    public AbstractEscaperoomLoadableDetachableModel() {
        super();
    }

    public AbstractEscaperoomLoadableDetachableModel(T object) {
        super(object);
    }

    @Override
    protected final T load() {
        Injector.get().inject(this);

        return loadInternal();
    }

    protected abstract T loadInternal();
}
