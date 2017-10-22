package services;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;
import dao.UserDao;
import model.User;
import play.mvc.Http.Session;

import javax.inject.Inject;

/**
 * Service layer for User DB entity
 */
public class UserService {

    private final PlayAuthenticate auth;

    private final UserDao userDao;

    @Inject
    public UserService(final PlayAuthenticate auth, UserDao userDao) {
        this.auth = auth;
        this.userDao = userDao;
    }

    public User getUser(Session session) {
        final AuthUser currentAuthUser = this.auth.getUser(session);

        if (currentAuthUser == null) {
            return User.createEmptyUser("", "guest@guest.com");
        }

        return userDao.findByAuthId(currentAuthUser.getId());
    }
}
