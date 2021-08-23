package de.kernebeck.escaperoom.escaperoomgame.webapp.dialog;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.GameExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.webapp.model.WorkflowListModel;
import de.kernebeck.escaperoom.escaperoomgame.webapp.pages.GamePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.ResourceModel;
import org.apache.wicket.request.mapper.parameter.PageParameters;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.ArrayList;
import java.util.List;

public abstract class CreateGameDialog extends AbstractDialog<String> {
    private static final long serialVersionUID = 1L;

    @SpringBean
    private transient GameService gameService;

    @SpringBean
    private transient GameExecutionService gameExecutionService;

    private Workflow selectedGame = null;
    private String player1 = null;
    private String player2 = null;
    private String player3 = null;
    private String player4 = null;
    private String errorMessage = "";


    private Form<?> form;
    private WebMarkupContainer feedback;

    public CreateGameDialog(String id, IModel<String> title) {
        super(id, title);


        this.form = new Form<>("dialogForm");

        this.feedback = new WebMarkupContainer("feedbackPanel");
        feedback.add(new Label("messageLabel", new PropertyModel<>(this, "errorMessage")));
        feedback.setOutputMarkupId(true);
        feedback.setOutputMarkupPlaceholderTag(true);
        feedback.setVisible(false);
        form.add(feedback);

        form.add(new Label("description", new ResourceModel("dialog.creategame.description", "dialog.creategame.description")));
        // Slider //
        form.add(new DropDownChoice<Workflow>("gameSelection", new PropertyModel<>(this, "selectedGame"), new WorkflowListModel(), new IChoiceRenderer<>() {
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

        }) {
            @Override
            protected CharSequence getDefaultChoice(String selectedValue) {
                return CreateGameDialog.this.getString("default.please.choose");
            }
        });
        form.add(new TextField<String>("player1", new PropertyModel<>(this, "player1")));
        form.add(new TextField<String>("player2", new PropertyModel<>(this, "player2")));
        form.add(new TextField<String>("player3", new PropertyModel<>(this, "player3")));
        form.add(new TextField<String>("player4", new PropertyModel<>(this, "player4")));

        form.add(new AjaxSubmitLink("startGame") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                if (selectedGame == null) {
                    errorMessage = "Es wurde kein Escaperoom ausgewählt!";
                    feedback.setVisible(true);
                    target.add(feedback);
                    return;
                }
                if (player1 == null || player1.isEmpty()) {
                    errorMessage = "Es wurde kein Spieler 1 angegeben.";
                    feedback.setVisible(true);
                    target.add(feedback);
                    return;
                }

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
                Injector.get().inject(CreateGameDialog.this);
                final Game game = gameService.createGame(selectedGame.getId(), users);
                gameExecutionService.startGame(game);
                final PageParameters pageParameters = new PageParameters();
                pageParameters.add("gameId", game.getGameId());
                closeDialog(target);
                setResponsePage(GamePage.class, pageParameters);

            }
        });

        form.add(new AjaxLink<Void>("closeDialog") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                CreateGameDialog.this.closeDialog(target);
            }

        });
        form.add(new AjaxLink<Void>("closeDialogX") {

            @Override
            public void onClick(AjaxRequestTarget target) {
                CreateGameDialog.this.closeDialog(target);
            }

        });
        this.add(form);
    }

}
