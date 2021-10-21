package de.kernebeck.escaperoom.escaperoomgame.core.service.authorisation.impl;

import de.kernebeck.escaperoom.escaperoomgame.AbstractUnitTest;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.entity.authorisation.User;
import de.kernebeck.escaperoom.escaperoomgame.core.datamodel.repository.authorisation.UserRespository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserServiceBeanUnitTest extends AbstractUnitTest {

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder(BCryptPasswordEncoder.BCryptVersion.$2Y);

    @Mock
    private UserRespository userRespository;

    @InjectMocks
    private UserServiceBean userServiceBean;

    @Test
    public void testExistsUser_Exists() {
        final User user = mock(User.class);
        when(userRespository.findUserByUsername("blub")).thenReturn(Optional.of(user));
        assertThat(userServiceBean.existsUser("blub")).isTrue();
    }

    @Test
    public void testExistsUser_ExistsNot() {
        when(userRespository.findUserByUsername("blub")).thenReturn(Optional.empty());
        assertThat(userServiceBean.existsUser("blub")).isFalse();
    }

    @Test
    public void testCreateUser_UserExists() {
        final User user = mock(User.class);
        when(userRespository.findUserByUsername("blub")).thenReturn(Optional.of(user));
        userServiceBean.createUser("blub", "blub", "blub", "asdf", true);
        verify(userRespository, times(0)).save(any(User.class));
    }

    @Test
    public void testCreateuser_UserNew() {
        when(userRespository.findUserByUsername("newuser")).thenReturn(Optional.empty());

        userServiceBean.createUser("newuser", "newuser", "newuser", "testpassword", true);
        verify(userRespository).save(any(User.class));
    }

    @Test
    public void testCheckAuthorisation_UserNotFound() {
        when(userRespository.findUserByUsername("newuser")).thenReturn(Optional.empty());
        assertThat(userServiceBean.checkAuthorisation("newuser", "blub")).isFalse();
    }

    @Test
    public void testCheckAuthorisation_Matching() {
        final User user = mock(User.class);
        when(user.getPassword()).thenReturn(passwordEncoder.encode("password"));
        when(userRespository.findUserByUsername("newuser")).thenReturn(Optional.of(user));
        assertThat(userServiceBean.checkAuthorisation("newuser", "password")).isTrue();
    }


}