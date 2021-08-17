package de.kernebeck.escaperoom.escaperoomgame.webservice.controller.ui.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.*;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.RiddleInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.WorkflowPartInstance;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.RiddleInstanceRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.SolutionService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.execution.GameExecutionService;
import de.kernebeck.escaperoom.escaperoomgame.webservice.resourceassembler.WorkflowPartResourceAssembler;
import de.kernebeck.escaperoom.escaperoomgame.webservice.resourceassembler.WorkflowResourceAssembler;
import de.kernebeck.escaperoom.escaperoomgame.webservice.resourceassembler.WorkflowTransitionResourceAssembler;
import de.kernebeck.escaperoom.escaperoomgame.webservice.resources.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.Reader;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@Transactional
@RequestMapping(value = "/api/ui/game")
public class GameController {

    private static final Logger LOGGER = LoggerFactory.getLogger(GameController.class);
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Autowired
    private WorkflowResourceAssembler workflowResourceAssembler;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private GameService gameService;

    @Autowired
    private GameExecutionService gameExecutionService;

    @Autowired
    private RiddleInstanceRepository riddleInstanceRepository;

    @Autowired
    private SolutionService solutionService;

    @Autowired
    private WorkflowTransitionResourceAssembler workflowTransitionResourceAssembler;

    @Autowired
    private WorkflowPartResourceAssembler workflowPartResourceAssembler;

    @GetMapping(path = "/getAvailableWorkflows", produces = "application/json")
    public ResponseEntity<List<WorkflowResource>> getAvailableWorkflows(Principal principal) {
        final List<Workflow> allWorkflows = workflowService.findAll();

        final List<WorkflowResource> result = new ArrayList<>();

        for (final Workflow w : allWorkflows) {
            result.add(workflowResourceAssembler.convertEntityToResource(w));
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping(value = "/createNewGame", consumes = "application/json", produces = "application/json")
    public ResponseEntity createNewGame(Principal principal, Reader reader) {
        try {
            final GameCreationResource gameCreationResource = OBJECT_MAPPER.readValue(reader, GameCreationResource.class);

            final List<String> errors = new ArrayList<>();

            if (gameCreationResource.getTechicalGameId() == null || workflowService.findById(gameCreationResource.getTechicalGameId()).isEmpty()) {
                errors.add("Die übergebene Escaperoomid " + (gameCreationResource.getTechicalGameId() == null ? "" : String.valueOf(gameCreationResource.getTechicalGameId())) + " ist leer oder es konnte kein " +
                        "entsprechendes Spiel in der Datenbank gefunden werden. Bitte wählen Sie ein Spiel aus oder kontaktieren Sie den Support.");
            }

            if (gameCreationResource.getUsernames() == null || gameCreationResource.getUsernames().isEmpty()) {
                errors.add("Es wurden keine Spielernamen mit übergeben. Bitte geben Sie mindestens einen Spielernamen ein!");
            }

            if (errors.isEmpty()) {
                final Game game = gameService.createGame(gameCreationResource.getTechicalGameId(), gameCreationResource.getUsernames());
                if (game != null) {
                    final GameResource result = new GameResource();
                    result.setGameId(game.getGameId());
                    return ResponseEntity.status(HttpStatus.CREATED).body(result);
                }
                return ResponseEntity.badRequest().body(new ErrorResource("Es ist ein unerwarteter Fehler bei der Anlage des Spiels aufgetreten. Bitte probieren Sie es erneut oder kontaktieren Sie den Support."));
            }

            return ResponseEntity.badRequest().body(new ErrorResource(errors));
        }
        catch (IOException e) {
            LOGGER.error("Unexpected parsing error! ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Es ist ein Fehler beim parsen der Daten aufgetreten! Bitte überprüfen Sie ihre Eingaben.");
        }
    }

    @PostMapping(value = "/{gameId}/start", produces = "application/json")
    public ResponseEntity startGame(Principal principal, @PathVariable String gameId) {
        final Game game = gameService.findNotFinishedByGameId(gameId);
        if (game == null) {
            ResponseEntity.badRequest().body(new ErrorResource("Es wurde kein aktives Spiel für die ID " + gameId + " gefunden."));
        }

        gameExecutionService.startGame(game);

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/{gameId}/pause", produces = "application/json")
    public ResponseEntity pauseGame(Principal principal, @PathVariable String gameId) {
        final Game game = gameService.findNotFinishedByGameId(gameId);
        if (game == null) {
            ResponseEntity.badRequest().body(new ErrorResource("Es wurde kein aktives Spiel für die ID " + gameId + " gefunden."));
        }

        gameExecutionService.startGame(game);

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/{gameId}/continue", produces = "application/json")
    public ResponseEntity continueGame(Principal principal, @PathVariable String gameId) {
        final Game game = gameService.findNotFinishedByGameId(gameId);
        if (game == null) {
            ResponseEntity.badRequest().body(new ErrorResource("Es wurde kein aktives Spiel für die ID " + gameId + " gefunden."));
        }

        gameExecutionService.startGame(game);

        return ResponseEntity.ok("");
    }

    @PostMapping(value = "/{gameId}/checkRiddleSolution/{riddleId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity checkRiddleSolution(Principal principal, @PathVariable String gameId, @PathVariable Long riddleId, Reader reader) {
        try {
            final CheckRiddleSolutionResource resource = OBJECT_MAPPER.readValue(reader, CheckRiddleSolutionResource.class);
            final Game game = gameService.findNotFinishedByGameId(gameId);
            if (game == null) {
                return ResponseEntity.badRequest().body(new ErrorResource("Es wurde kein aktives Spiel für die ID " + gameId + " gefunden."));
            }

            //check if current workflowpart of game has riddle which should be checked
            if (game.getActiveWorkflowPartInstance().getWorkflowPart().getRiddles().stream().noneMatch(r -> r.getId().equals(riddleId))) {
                return ResponseEntity.badRequest().body(new ErrorResource("Das Rätsel steht im aktuellen Spielstatus nicht zur Verfügung."));
            }
            else if (game.getActiveWorkflowPartInstance().getRiddleInstanceList() != null && game.getActiveWorkflowPartInstance().getRiddleInstanceList().stream().anyMatch(r -> r.getRiddle().getId().equals(riddleId) && r.isResolved())) {
                return ResponseEntity.badRequest().body(new ErrorResource("Das Rätsel wurde bereits gelöst!"));
            }

            final List<Solution> solutions = solutionService.findByRiddleId(riddleId);

            //check if one valid solution exsits
            final Optional<Solution> solution = solutions.stream().filter(s -> s.getSolution().equals(resource.getSolution())).findFirst();
            final RiddleInstance riddleInstance = getOrSolvedRiddleByRiddleIdAndWorkflowpart(riddleId, game.getActiveWorkflowPartInstance());
            riddleInstance.setAttempts(riddleInstance.getAttempts() + 1);
            if (solution.isPresent()) {
                riddleInstance.setResolved(Boolean.TRUE);
            }
            riddleInstanceRepository.save(riddleInstance);

            return ResponseEntity.ok(new CheckRiddleResult(riddleInstance.isResolved()));

        }
        catch (IOException e) {
            LOGGER.error("Invalid json format! " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occured! " + e.getMessage());
        }
    }

    @PostMapping(value = "/{gameId}/getNextRiddleHint/{riddleId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity getNextRiddleHint(Principal principal, @PathVariable String gameId, @PathVariable Long riddleId) {
        try {
            final Game game = gameService.findNotFinishedByGameId(gameId);
            if (game == null) {
                return ResponseEntity.badRequest().body(new ErrorResource("Es wurde kein aktives Spiel für die ID " + gameId + " gefunden."));
            }

            if (game.getActiveWorkflowPartInstance().getWorkflowPart().getRiddles().stream().noneMatch(r -> r.getId().equals(riddleId))) {
                return ResponseEntity.badRequest().body(new ErrorResource("Das Rätsel zum angeforderten Hinweis steht im aktuellen Spielstatus nicht zur Verfügung."));
            }

            final RiddleInstance riddleInstance = getOrSolvedRiddleByRiddleIdAndWorkflowpart(riddleId, game.getActiveWorkflowPartInstance());
            if (riddleInstance == null) {
                return ResponseEntity.badRequest().body(new ErrorResource("Die Hinweise zum Rätsel stehen aktuell nicht zur Verfügung"));
            }
            if (riddleInstance.isResolved()) {
                return ResponseEntity.badRequest().body(new ErrorResource("Das Rätsel ist bereits gelöst."));
            }

            if (riddleInstance.getUsedHints().size() == riddleInstance.getRiddle().getHints().size()) {
                return ResponseEntity.badRequest().body(new ErrorResource("Es wurden bereits alle Hinweise für das Rätsel abgerufen"));
            }

            final Optional<RiddleHint> highestSortIndexRiddleHint = riddleInstance.getUsedHints().stream().max(Comparator.comparingInt((t) -> t.getSortIndex() == null ? 0 : t.getSortIndex()));
            final RiddleHint hint2Use;
            if (highestSortIndexRiddleHint.isEmpty()) {
                Optional<RiddleHint> hint = riddleInstance.getRiddle().getHints().stream().sorted(Comparator.comparingInt((t) -> t.getSortIndex() == null ? 0 : t.getSortIndex())).findFirst();
                if (hint.isPresent()) {
                    hint2Use = hint.get();
                    riddleInstance.addUsedHint(hint.get());
                }
                else {
                    return ResponseEntity.badRequest().body(new ErrorResource("Das Rätsel besitzt keine Hinweise.!"));
                }
            }
            else {
                final Set<Long> usedHints = riddleInstance.getUsedHints().stream().map(RiddleHint::getId).collect(Collectors.toSet());

                Optional<RiddleHint> hint = riddleInstance.getRiddle().getHints().stream().filter(sh -> !usedHints.contains(sh.getId())).sorted(Comparator.comparingInt((t) -> t.getSortIndex() == null ? 0 : t.getSortIndex())).findFirst();
                hint2Use = hint.get();
                riddleInstance.addUsedHint(hint.get());
            }
            riddleInstanceRepository.save(riddleInstance);

            return ResponseEntity.ok(new RiddleHintResource(hint2Use));
        }
        catch (Exception e) {
            LOGGER.error("Invalid json format! " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResource("Unexpected error occured! " + e.getMessage()));
        }
    }

    @PostMapping(value = "{gameId}/getNextPossibleWorkflowTransitions", produces = "application/json")
    public ResponseEntity getNextPossibleWorkflowTransitions(Principal principal, @PathVariable String gameId) {
        final Game game = gameService.findNotFinishedByGameId(gameId);
        if (game == null) {
            return ResponseEntity.badRequest().body(new ErrorResource("Es wurde kein aktives Spiel für die ID " + gameId + " gefunden."));
        }

        //first check if all riddles are resolved
        if (game.getActiveWorkflowPartInstance().getRiddleInstanceList().stream().anyMatch(r -> !r.isResolved())) {
            return ResponseEntity.badRequest().body(new ErrorResource("Es sind nicht alle Rätsel gelöst."));
        }

        //second check if any riddle is not resolved
        final Set<WorkflowTransition> transitions = game.getActiveWorkflowPartInstance().getWorkflowPart().getOutgoingTransitions();
        if (transitions == null || transitions.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResource("Es konnten keine folgenden Workflowtransitionen gefunden werden."));
        }
        final List<WorkflowTransition> transitionsSorted = new ArrayList<>(game.getActiveWorkflowPartInstance().getWorkflowPart().getOutgoingTransitions());

        return ResponseEntity.ok(transitionsSorted.stream().map(workflowTransitionResourceAssembler::convertEntityToResource).sorted(Comparator.comparingInt(WorkflowTransitionResource::getSortIndex)).collect(Collectors.toList()));
    }

    @PostMapping(value = "{gameId}/executeChoosenWorkflowTransition/{workflowTransitionId}", produces = "application/json")
    public ResponseEntity executeChoosenWorkflowTransition(Principal principal, @PathVariable String gameId, @PathVariable Long workflowTransitionId) {
        final Game game = gameService.findNotFinishedByGameId(gameId);
        if (game == null) {
            return ResponseEntity.badRequest().body(new ErrorResource("Es wurde kein aktives Spiel für die ID " + gameId + " gefunden."));
        }

        //first check if all riddles are resolved
        //first check if all riddles are resolved
        if (game.getActiveWorkflowPartInstance().getRiddleInstanceList().stream().anyMatch(r -> !r.isResolved())) {
            return ResponseEntity.badRequest().body(new ErrorResource("Es sind nicht alle Rätsel gelöst."));
        }

        final Optional<WorkflowTransition> transition = game.getActiveWorkflowPartInstance().getWorkflowPart().getOutgoingTransitions().stream().filter(t -> t.getId().equals(workflowTransitionId)).findFirst();

        if (transition.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResource("Die angegebene Workflowtransition ist für den aktuellen Spielstatus nicht gültig!"));
        }

        final boolean success = gameExecutionService.executeWorkflowTransition(game, transition.get());

        if (success) {
            return ResponseEntity.ok(workflowPartResourceAssembler.convertEntityToResource(gameService.findByGameId(game.getGameId()).getActiveWorkflowPartInstance()));
        }
        return ResponseEntity.badRequest().body("Die angegebene Workflowtransition konnte nicht ausgeführt werden.");
    }

    private RiddleInstance getOrSolvedRiddleByRiddleIdAndWorkflowpart(Long riddleId, WorkflowPartInstance part) {
        final Optional<RiddleInstance> solvedRiddleOptional = part.getRiddleInstanceList().stream().filter(r -> r.getRiddle().getId().equals(riddleId)).findFirst();
        RiddleInstance riddleInstance = null;
        if (solvedRiddleOptional.isEmpty()) {
            final Optional<Riddle> riddle = part.getWorkflowPart().getRiddles().stream().filter(r -> r.getId().equals(riddleId)).findFirst();
            if (riddle.isPresent()) {
                riddleInstance = riddleInstanceRepository.save(new RiddleInstance(riddle.get(), Collections.emptyList(), part, 0, Boolean.FALSE));
            }
        }
        else {
            riddleInstance = solvedRiddleOptional.get();
        }

        return riddleInstance;
    }

}
