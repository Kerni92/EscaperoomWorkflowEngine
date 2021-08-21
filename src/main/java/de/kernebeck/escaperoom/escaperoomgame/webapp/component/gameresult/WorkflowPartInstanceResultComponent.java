package de.kernebeck.escaperoom.escaperoomgame.webapp.component.gameresult;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult.RiddleResultDTO;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult.WorkflowPartResultDTO;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.GenericPanel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.util.ListModel;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class WorkflowPartInstanceResultComponent extends GenericPanel<WorkflowPartResultDTO> {

    public WorkflowPartInstanceResultComponent(String id, IModel<WorkflowPartResultDTO> model) {
        super(id, model);

        final WebMarkupContainer content = new WebMarkupContainer("content");

        content.add(new Label("partName", Model.of(model.getObject().getName())));
        content.add(new Label("neededTime", Model.of("Benötigte Zeit: " + model.getObject().getElapsedTimeInMinutes() + " Minuten")));

        final List<RiddleResultDTO> riddleList = new ArrayList<>(model.getObject().getRiddleInformations());
        riddleList.sort(Comparator.comparingInt(RiddleResultDTO::getSortIndex));
        final ListModel<RiddleResultDTO> riddleInformationsModel = new ListModel<>(riddleList);
        final ListView<RiddleResultDTO> listView = new ListView<RiddleResultDTO>("riddles", riddleInformationsModel) {
            @Override
            protected void populateItem(ListItem<RiddleResultDTO> item) {
                final RiddleResultDTO resultDTO = item.getModelObject();
                item.add(new Label("riddleName", Model.of(resultDTO.getName())));
                item.add(new Label("attempts", Model.of(resultDTO.getAttempts())));
                item.add(new Label("usedHints", Model.of(resultDTO.getUsedHints())));
            }
        };
        content.add(listView);

        add(content);
    }


}
