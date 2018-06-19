package services;

import common.SessionParams;
import dao.UserDao;
import model.User;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.oauth.profile.google2.Google2Profile;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.store.PlaySessionStore;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for User DB entity
 */
public class UserService {
    private static final Logger.ALogger log = Logger.of(UserService.class);

    private final UserDao userDao;

    private final PlaySessionStore playSessionStore;


    @Inject
    public UserService(UserDao userDao, PlaySessionStore playSessionStore) {
        this.userDao = userDao;
        this.playSessionStore = playSessionStore;
    }

    public Optional<User> getUser(Http.Session session) {
        return userDao.findByAuthId(session.get(SessionParams.USER_AUTH_ID));
    }

    public Optional<User> getOrCreateUser(String userId, String email) {
        Optional<User> existingUser = userDao.findByAuthId(userId);

        return existingUser.or(
                () -> {
                    User user = User.createEmptyUser(userId, email);
                    userDao.save(user);

                    return Optional.ofNullable(user);
                });
    }
}
