package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.WorkflowRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class WorkflowServiceBean implements WorkflowService {

    @Autowired
    private WorkflowRepository workflowRepository;

    @Override
    public List<Workflow> findAll() {
        final List<Workflow> result = new ArrayList<>();

        final Iterable<Workflow> foundWfs = workflowRepository.findAll();
        for (Workflow w : foundWfs) {
            result.add(w);
        }
        result.sort(Comparator.comparingLong(Workflow::getId));

        return result;
    }

    @Override
    public Optional<Workflow> findById(Long id) {
        return workflowRepository.findById(id);
    }
}
