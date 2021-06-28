package de.kernebeck.escaperoom.escaperoomgame.webservice.controller.ui.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Solution;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Workflow;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.Game;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.execution.SolvedRiddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.execution.SolvedRiddleRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.GameService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.SolutionService;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.WorkflowService;
import de.kernebeck.escaperoom.escaperoomgame.webservice.resourceassembler.WorkflowResourceAssembler;
import de.kernebeck.escaperoom.escaperoomgame.webservice.resources.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.Reader;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
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
    private RiddleService riddleService;

    @Autowired
    private SolvedRiddleRepository solvedRiddleRepository;

    @Autowired
    private SolutionService solutionService;

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
                return ResponseEntity.badRequest().body("Es ist ein unerwarteter Fehler bei der Anlage des Spiels aufgetreten. Bitte probieren Sie es erneut oder kontaktieren Sie den Support.");
            }

            return ResponseEntity.badRequest().body(errors);
        }
        catch (IOException e) {
            LOGGER.error("Unexpected parsing error! ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Es ist ein Fehler beim parsen der Daten aufgetreten! Bitte überprüfen Sie ihre Eingaben.");
        }
    }

    @PostMapping(value = "/checkRiddleSolution", consumes = "application/json", produces = "application/json")
    public ResponseEntity checkRiddleSolution(Principal principal, Reader reader) {
        try {
            final CheckRiddleSolutionResource resource = OBJECT_MAPPER.readValue(reader, CheckRiddleSolutionResource.class);
            final List<String> errors = new ArrayList<>();
            final Game game = gameService.findNotFinishedByGameId(resource.getGameId());
            if (game == null) {
                errors.add("Es wurde aktives Spiel für die ID " + resource.getGameId() + " gefunden.");
            }

            //check if current workflowpart of game has riddle which should be checked
            if (game.getCurrentWorkflowpart().getWorkflowPart().getRiddles().stream().noneMatch(r -> r.getId().equals(resource.getRiddleId()))) {
                errors.add("Das Rätsel steht im aktuellen Spielstatus nicht zur Verfügung.");
            }
            else if (game.getCurrentWorkflowpart().getSolvedRiddleList() != null && game.getCurrentWorkflowpart().getSolvedRiddleList().stream().anyMatch(r -> r.getRiddle().getId().equals(resource.getRiddleId()) && r.getResolved())) {
                errors.add("Das Rätsel wurde bereits gelöst!");
            }

            if (errors.isEmpty()) {
                final List<Solution> solutions = solutionService.findByRiddleId(resource.getRiddleId());

                //check if one valid solution exsits
                final Optional<Solution> solution = solutions.stream().filter(s -> s.getSolution().equals(resource.getSolution())).findFirst();

                final Optional<SolvedRiddle> solvedRiddleOptional = game.getCurrentWorkflowpart().getSolvedRiddleList().stream().filter(r -> r.getRiddle().getId().equals(resource.getRiddleId())).findFirst();
                final SolvedRiddle solvedRiddle = solvedRiddleOptional.orElse(new SolvedRiddle(riddleService.findById(resource.getRiddleId()), Collections.emptyList(), game.getCurrentWorkflowpart(), 0, false));
                solvedRiddle.setAttempts(solvedRiddle.getAttempts() + 1);
                if (solution.isPresent()) {
                    solvedRiddle.setResolved(Boolean.TRUE);
                }
                solvedRiddleRepository.save(solvedRiddle);

                return ResponseEntity.ok(new CheckRiddleResult(solvedRiddle.getResolved()));
            }

            return ResponseEntity.badRequest().body(errors);
        }
        catch (IOException e) {
            LOGGER.error("Invalid json format! " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occured! " + e.getMessage());
        }
    }

    @PostMapping(value = "/getNextRiddleHint", consumes = "application/json", produces = "application/json")
    public ResponseEntity getNextRiddleHint(Principal principal, Reader reader) {
        try {
            final GetNextRiddleHintResource resource = OBJECT_MAPPER.readValue(reader, GetNextRiddleHintResource.class);
            final List<String> errors = new ArrayList<>();

            final Game game = gameService.findNotFinishedByGameId(resource.getGameId());
            if (game == null) {
                errors.add("Es wurde aktives Spiel für die ID " + resource.getGameId() + " gefunden.");
            }

            if (game.getCurrentWorkflowpart().getWorkflowPart().getRiddles().stream().noneMatch(r -> r.getId().equals(resource.getRiddleId()))) {
                errors.add("Das Rätsel zum angeforderten Hinweis steht im aktuellen Spielstatus nicht zur Verfügung.");
            }

            if (errors.isEmpty()) {

            }

        }
        catch (IOException e) {
            LOGGER.error("Invalid json format! " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected error occured! " + e.getMessage());
        }
    }

}
