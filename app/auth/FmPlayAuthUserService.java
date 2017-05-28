package auth;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.oauth2.google.GoogleAuthUser;
import com.feth.play.module.pa.service.AbstractUserService;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import dao.UserDao;
import model.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FmPlayAuthUserService extends AbstractUserService {
	private final UserDao userDao;

	@Inject
	public FmPlayAuthUserService(final PlayAuthenticate auth, UserDao userDao) {
		super(auth);

		this.userDao = userDao;
	}

	@Override
	public String save(final AuthUser authUser) {
        GoogleAuthUser googleAuthUser = AuthUserConverter.toGoogleAuthUser(authUser);
        if (googleAuthUser == null) {
            return null;
        }

        User user = User.createEmptyUser(googleAuthUser.getId(), googleAuthUser.getEmail());

		if (!userDao.idExistsInDb(user.getId())) {
			userDao.save(user);
			return user.getId();
		} else {
			return null;
		}
	}

	@Override
    public String getLocalIdentity(final AuthUserIdentity identity) {
		// For production: Caching might be a good idea here...
		// ...and dont forget to sync the cache when users get deactivated/deleted

        final User user = userDao.findByAuthId(identity.getId());

		return user != null ? user.getId() : null;
	}

	@Override
	public AuthUser merge(final AuthUser newUser, final AuthUser oldUser) {
//		if (!oldUser.equals(newUser)) {
//			User.merge(oldUser, newUser);
//		}

		return oldUser;
	}

	@Override
	public AuthUser link(final AuthUser oldUser, final AuthUser newUser) {
//		User.addLinkedAccount(oldUser, newUser);

		return null;
	}

}
