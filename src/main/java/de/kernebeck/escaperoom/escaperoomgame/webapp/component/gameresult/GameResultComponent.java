package de.kernebeck.escaperoom.escaperoomgame.webapp.component.gameresult;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult.GameResultDTO;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult.WorkflowPartResultDTO;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.webapp.pages.HomePage;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GameResultComponent extends GenericPanel<Game> {

    @SpringBean
    private GameService gameService;

    public GameResultComponent(String id, IModel<Game> model) {
        super(id, model);
    }

    @Override
    protected void onInitialize() {
        super.onInitialize();

        final GameResultDTO resultDTO = gameService.calculateGameResultInformationForGame(getModelObject().getId());
        final SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
        final WebMarkupContainer content = new WebMarkupContainer("content");

        content.add(new Label("gratulation", Model.of("Ihr habt es geschafft und seid erfolgreich aus <em> " + resultDTO.getName() + "</em> entkommen!<br/><br/>Nun könnt Ihr euch in Ruhe eure Spielstatistik anschauen.<br/><br/>" +
                "Wir hoffen es hat euch Spass gemacht und bis Bald :)")).setEscapeModelStrings(false));
        content.add(new Label("startTime", Model.of("Startzeit: " + format.format(resultDTO.getStartTime()))));
        content.add(new Label("endTime", Model.of("Endzeit: " + format.format(resultDTO.getEndTime()))));
        content.add(new Label("timeInMinutes", Model.of("Benötige Zeit in Minuten: " + resultDTO.getElapsedTimeInMinutes())));

        final RepeatingView workflowParts = new RepeatingView("workflowPartInformations");
        final List<WorkflowPartResultDTO> playedWorkflowParts = new ArrayList<>(resultDTO.getPlayedWorkflowParts());
        playedWorkflowParts.sort(Comparator.comparingLong(WorkflowPartResultDTO::getId));
        for (final WorkflowPartResultDTO workflowPartResultDTO : resultDTO.getPlayedWorkflowParts()) {
            workflowParts.add(new WorkflowPartInstanceResultComponent(workflowParts.newChildId(), Model.of(workflowPartResultDTO)));
        }
        content.add(workflowParts);

        final Form backToHomeForm = new Form("backToHomeForm");
        backToHomeForm.add(new AjaxButton("backToHomeButton") {
            @Override
            protected void onSubmit(AjaxRequestTarget target) {
                super.onSubmit(target);
                setResponsePage(HomePage.class);
            }
        });
        content.add(backToHomeForm);

        add(content);
    }
}
