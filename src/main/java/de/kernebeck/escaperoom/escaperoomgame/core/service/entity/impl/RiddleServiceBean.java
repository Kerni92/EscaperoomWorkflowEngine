package de.kernebeck.escaperoom.escaperoomgame.core.service.entity.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.definition.Riddle;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.definition.RiddleRepository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.entity.RiddleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RiddleServiceBean implements RiddleService {

    @Autowired
    private RiddleRepository riddleRepository;

    @Override
    public Riddle findById(Long id) {
        final Optional<Riddle> riddle = riddleRepository.findById(id);
        return riddle.orElse(null);
    }
}
