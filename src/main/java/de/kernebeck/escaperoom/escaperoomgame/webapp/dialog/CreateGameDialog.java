package de.kernebeck.escaperoom.escaperoomgame.webapp.dialog;

import com.googlecode.wicket.jquery.ui.JQueryIcon;
import com.googlecode.wicket.jquery.ui.form.dropdown.DropDownChoice;
import com.googlecode.wicket.jquery.ui.panel.JQueryFeedbackPanel;
import com.googlecode.wicket.jquery.ui.widget.dialog.AbstractFormDialog;
import com.googlecode.wicket.jquery.ui.widget.dialog.DialogButton;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowService;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.WorkflowListModel;
import de.kernebeck.escaperoom.escaperoomgame.webapp.pages.GamePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.core.request.handler.IPartialPageRequestHandler;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CreateGameDialog<T extends Serializable> extends AbstractFormDialog<T> {
    private static final long serialVersionUID = 1L;

    private final DialogButton btnSubmit = new DialogButton(SUBMIT, new ResourceModel("homepage.dialog.startgame", "homepage.dialog.startgame"), JQueryIcon.CHECK);
    private final DialogButton btnCancel = new DialogButton(CANCEL, LBL_CANCEL, JQueryIcon.CANCEL);

    @SpringBean
    private WorkflowService workflowService;

    @SpringBean
    private GameService gameService;

    private Workflow selectedGame = null;
    private String player1 = null;
    private String player2 = null;
    private String player3 = null;
    private String player4 = null;


    private Form<?> form;
    private FeedbackPanel feedback;

    public CreateGameDialog(String id, IModel<String> title) {
        super(id, title, true);

        this.form = new Form<>("dialogForm");
        this.add(form);

        form.add(new Label("description", new ResourceModel("dialog.creategame.description", "dialog.creategame.description")));
        // Slider //
        form.add(new Label("lblGameSelection", new ResourceModel("dialog.creategame.selectGame", "dialog.creategame.selectGame")));
        form.add(new DropDownChoice<Workflow>("gameSelection", new PropertyModel<>(this, "selectedGame"), new WorkflowListModel(workflowService), new IChoiceRenderer<>() {
            @Override
            public Object getDisplayValue(Workflow object) {
                return object.getName();
            }

            @Override
            public String getIdValue(Workflow object, int index) {
                return object.getId().toString();
            }

            @Override
            public Workflow getObject(String id, IModel<? extends List<? extends Workflow>> choices) {
                return choices.getObject().stream().filter(w -> w.getId().toString().equals(id)).findFirst().orElse(null);
            }

        }));
        form.add(new Label("lblPlayer1", new ResourceModel("dialog.creategame.player1", "dialog.creategame.player1")));
        form.add(new RequiredTextField<String>("player1", new PropertyModel<>(this, "player1")));
        form.add(new Label("lblPlayer2", new ResourceModel("dialog.creategame.player2", "dialog.creategame.player2")));
        form.add(new TextField<String>("player2", new PropertyModel<>(this, "player2")));
        form.add(new Label("lblPlayer3", new ResourceModel("dialog.creategame.player3", "dialog.creategame.player3")));
        form.add(new TextField<String>("player3", new PropertyModel<>(this, "player3")));
        form.add(new Label("lblPlayer4", new ResourceModel("dialog.creategame.player4", "dialog.creategame.player4")));
        form.add(new TextField<String>("player4", new PropertyModel<>(this, "player4")));

        this.feedback = new JQueryFeedbackPanel("feedbackPanel");
        feedback.setOutputMarkupId(true);
        add(feedback);
    }

    @Override
    protected void onSubmit(AjaxRequestTarget target, DialogButton button) {
        if (selectedGame == null) {
            this.error(new ResourceModel("error", "error").getObject());
            return;
        }
        if (button.equals(getSubmitButton())) {
            final List<String> users = new ArrayList<>();
            users.add(player1);
            if (player2 != null && !player2.isEmpty()) {
                users.add(player2);
            }
            if (player3 != null && !player3.isEmpty()) {
                users.add(player3);
            }
            if (player4 != null && !player4.isEmpty()) {
                users.add(player4);
            }
            final Game game = gameService.createGame(selectedGame.getId(), users);

            final PageParameters pageParameters = new PageParameters();
            pageParameters.add("gameId", game.getGameId());
            close(target, button);
            setResponsePage(GamePage.class, pageParameters);
        }
    }

    @Override
    protected List<DialogButton> getButtons() {
        return Arrays.asList(this.btnSubmit, this.btnCancel);
    }

    @Override
    public DialogButton getSubmitButton() {
        return this.btnSubmit;
    }

    @Override
    public Form<?> getForm() {
        return this.form;
    }

    // Events //

    @Override
    protected void onOpen(IPartialPageRequestHandler handler) {
        handler.add(this.form);
    }

    @Override
    public void onError(AjaxRequestTarget target, DialogButton button) {
        target.add(this.feedback);
    }
}
