
package de.kernebeck.escaperoom.escaperoomgame.webapp.dialog;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.GameExecutionService;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

public abstract class PauseGameDialog extends AbstractDialog<Game> {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private transient GameExecutionService gameExecutionService;

    public PauseGameDialog(String id, IModel<Game> gameIModel) {
        super(id, gameIModel);

        final Form<Void> form = new Form<>("dialogForm");

        final AjaxSubmitLink continueGameButton = new AjaxSubmitLink("continueGameBtn") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                Injector.get().inject(PauseGameDialog.this);

                gameExecutionService.continueGame(PauseGameDialog.this.getModelObject());
                closeDialog(target);
            }
        };
        form.add(continueGameButton);

        this.add(form);
    }

    @Override
    public void updateDialog(IPartialPageRequestHandler target) {
        //nothing todo
    }
}
