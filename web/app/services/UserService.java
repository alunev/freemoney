package services;

import dao.UserDao;
import model.User;
import play.mvc.Http.Session;

import javax.inject.Inject;

/**
 * Service layer for User DB entity
 */
public class UserService {

    private final UserDao userDao;

    @Inject
    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public User getUser(Session session) {
//        final AuthUser currentAuthUser = this.auth.getUser(session);
//
//        if (currentAuthUser == null) {
//            return User.GUEST;
//        }

//        return userDao.findByAuthId(currentAuthUser.getId());
        return User.GUEST;
    }
}
