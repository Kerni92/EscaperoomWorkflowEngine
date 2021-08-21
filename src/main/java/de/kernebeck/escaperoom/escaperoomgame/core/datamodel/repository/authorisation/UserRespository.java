package de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.authorisation;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.authorisation.User;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.BasicEntityRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRespository extends BasicEntityRepository<User> {

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByUsernameAndPassword(String username, String password);

}
