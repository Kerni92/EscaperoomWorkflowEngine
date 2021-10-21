package de.kernebeck.escaperoom.escaperoomgame.core.service.authorisation.impl;

import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.authorisation.User;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.authorisation.UserRespository;
import de.kernebeck.escaperoom.escaperoomgame.core.service.authorisation.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceBean implements UserService {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y);

    @Autowired
    private UserRespository userRespository;

    @Override
    public boolean existsUser(String username) {
        return userRespository.findUserByUsername(username).isPresent();
    }

    @Override
    public void createUser(String firstname, String lastname, String username, String password, boolean isEnabled) {
        if (userRespository.findUserByUsername(username).isPresent()) {
            return;
        }

        final User user = new User(firstname, lastname, username, passwordEncoder.encode(password), isEnabled);
        userRespository.save(user);
    }

    @Override
    public boolean checkAuthorisation(String username, String password) {
        final Optional<User> user = userRespository.findUserByUsername(username);
        if (user.isPresent()) {
            return passwordEncoder.matches(password, user.get().getPassword());
        }
        return false;
    }
}
