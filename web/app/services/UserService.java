package services;

import dao.UserDao;
import model.User;
import org.pac4j.core.profile.ProfileManager;
import org.pac4j.oauth.profile.google2.Google2Profile;
import org.pac4j.play.PlayWebContext;
import org.pac4j.play.store.PlaySessionStore;
import play.Logger;
import play.mvc.Controller;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;
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

    public Optional<User> getUser() {
        List<Google2Profile> profiles = getProfiles();

        log.debug("Profiles: " + profiles);

        Optional<User> existingUser = profiles.stream()
                .map(google2Profile -> userDao.findByAuthId(google2Profile.getId()))
                .filter(Objects::nonNull)
                .findFirst();

        return existingUser.or(
                () -> profiles.stream()
                        .findFirst()
                        .map(google2Profile -> {
                            User user = User.createEmptyUser(google2Profile.getId(), google2Profile.getEmail());
                            userDao.save(user);

                            return user;
                        })
        );
    }

    private List<Google2Profile> getProfiles() {
        final PlayWebContext context = new PlayWebContext(Controller.ctx(), playSessionStore);
        final ProfileManager<Google2Profile> profileManager = new ProfileManager<>(context);

        return profileManager.getAll(true);
    }
}
