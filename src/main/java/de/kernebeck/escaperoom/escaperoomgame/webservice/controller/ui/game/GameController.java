package de.kernebeck.escaperoom.escaperoomgame.webservice.controller.ui.game;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowService;
import de.kernebeck.escaperoom.escaperoomgame.webservice.resourceassembler.WorkflowResourceAssembler;
import de.kernebeck.escaperoom.escaperoomgame.webservice.resources.WorkflowResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/ui/game")
public class GameController {

    @Autowired
    private WorkflowResourceAssembler workflowResourceAssembler;

    @Autowired
    private WorkflowService workflowService;


    @GetMapping(path = "/getAvailableWorkflows", produces = "application/json")
    public ResponseEntity<List<WorkflowResource>> getAvailableWorkflows(Principal principal) {
        final List<Workflow> allWorkflows = workflowService.findAll();

        final List<WorkflowResource> result = new ArrayList<>();

        for (final Workflow w : allWorkflows) {
            result.add(workflowResourceAssembler.convertEntityToResource(w));
        }

        return ResponseEntity.ok(result);
    }

}
