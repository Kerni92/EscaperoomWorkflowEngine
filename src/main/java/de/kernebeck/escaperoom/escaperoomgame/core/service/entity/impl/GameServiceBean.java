package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.GameRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.WorkflowPartInstanceRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@Transactional
public class GameServiceBean implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private WorkflowPartInstanceRepository workflowPartInstanceRepository;

    @Override
    public Game createGame(Long workflowId, List<String> usernames) {
        final Optional<Workflow> wf = workflowService.findById(workflowId);
        if (wf.isPresent()) {
            Game game = new Game(UUID.randomUUID().toString(), null, null, null, wf.get());
            game.setUsernames(usernames);
            game.setExecutedWorkflowParts(Collections.emptyList());

            game = gameRepository.save(game);

            WorkflowPartInstance workflowPartInstance = workflowPartInstanceRepository.save(new WorkflowPartInstance(wf.get().getWorkflowStart(), null, null, null, null, game));
            workflowPartInstance = workflowPartInstanceRepository.save(workflowPartInstance);
            game.setCurrentWorkflowpart(workflowPartInstance);

            return gameRepository.save(game);
        }

        return null;
    }

    @Override
    public Game findNotFinishedByGameId(String gameId) {
        final Optional<Game> game = gameRepository.findByGameIdAndFinishedFalse(gameId);
        return game.orElse(null);
    }

    @Override
    public Game save(Game game) {
        if (game != null) {
            return gameRepository.save(game);
        }
        return null;
    }
}
