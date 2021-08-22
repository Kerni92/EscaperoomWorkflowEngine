package de.kernebeck.escaperoom.escaperoomgame.webapp.dialog;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;

public abstract class AbstractDialog<T> extends GenericPanel<T> {

    public AbstractDialog(String id) {
        super(id);
    }

    public AbstractDialog(String id, IModel<T> model) {
        super(id, model);
    }

    public abstract void closeDialog(AjaxRequestTarget target);

    public abstract void updateDialog(IPartialPageRequestHandler target);
}
