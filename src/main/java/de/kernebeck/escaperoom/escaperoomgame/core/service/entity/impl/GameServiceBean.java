package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult.GameResultDTO;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult.RiddleResultDTO;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.dto.gameresult.WorkflowPartResultDTO;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.GameRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleInstanceService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowPartInstanceService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.WorkflowExecutionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.*;

@Component
@Transactional
public class GameServiceBean implements GameService {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private WorkflowPartInstanceService workflowPartInstanceService;

    @Autowired
    private WorkflowExecutionService workflowExecutionService;

    @Autowired
    private RiddleInstanceService riddleInstanceService;

    @Override
    public Game createGame(Long workflowId, List<String> usernames) {
        final Optional<Workflow> wf = workflowService.findById(workflowId);
        if (wf.isPresent()) {
            Game game = new Game(UUID.randomUUID().toString(), null, null, null, Boolean.FALSE, null, wf.get());
            game.setUsernames(usernames);
            game.setExecutedWorkflowParts(Collections.emptyList());

            game = gameRepository.save(game);

            final WorkflowPartInstance workflowPartInstance = workflowPartInstanceService.createWorkflowPartInstanceFromWorkflowPart(game, wf.get().getWorkflowStart());
            game.setCurrentWorkflowpart(workflowPartInstance);

            return gameRepository.save(game);
        }

        return null;
    }

    @Transactional
    @Override
    public GameResultDTO calculateGameResultInformationForGame(Long gameId) {
        final Game game = gameRepository.findById(gameId).orElse(null);
        if (game != null) {
            final List<WorkflowPartResultDTO> workflowPartResultDTOS = new ArrayList<>();
            for (final WorkflowPartInstance wpi : game.getExecutedWorkflowParts()) {
                final List<RiddleResultDTO> riddleResultDTOS = new ArrayList<>();
                for (final RiddleInstance ri : wpi.getRiddleInstanceList()) {
                    riddleResultDTOS.add(new RiddleResultDTO(ri.getRiddle().getName(), ri.getRiddle().getSortIndex(), ri.getAttempts()));
                }
                final long elapsedTimeInMinutesForWorkflowPartInstance = wpi.getTotalTime() / (1000 * 60); //1000ms = 1sek * 60 sek pro minute
                workflowPartResultDTOS.add(new WorkflowPartResultDTO(wpi.getWorkflowPart().getName(), elapsedTimeInMinutesForWorkflowPartInstance, riddleResultDTOS));
            }

            final long elapsedTimeForGameInMinutes = game.getTotalTime() / (1000 * 60);
            return new GameResultDTO(game.getWorkflow().getName(), game.getGameId(), game.getStarttime(), game.getEndTime(), elapsedTimeForGameInMinutes, workflowPartResultDTOS);
        }

        return null;
    }

    @Override
    public Game findByGameId(String gameId) {
        if (gameId != null) {
            final Optional<Game> result = gameRepository.findByGameId(gameId);
            if (result.isPresent()) {
                return result.get();
            }
        }
        return null;
    }

    @Override
    public Game findGameByRiddleInstance(RiddleInstance riddleInstance) {
        if (riddleInstance != null) {
            final Optional<Game> result = gameRepository.findGameByRiddleInstance(riddleInstance.getId());
            if (result.isPresent()) {
                return result.get();
            }
        }
        return null;
    }


    @Override
    public Game findNotFinishedByGameId(String gameId) {
        final Optional<Game> game = gameRepository.findByGameIdAndFinishedFalse(gameId);
        return game.orElse(null);
    }

    @Override
    public Game load(Long id) {
        final Optional<Game> game = gameRepository.findById(id);
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
