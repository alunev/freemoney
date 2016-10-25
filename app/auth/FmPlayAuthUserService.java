package auth;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.oauth2.google.GoogleAuthUser;
import com.feth.play.module.pa.service.AbstractUserService;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import model.User;
import model.UserHelper;
import play.db.jpa.Transactional;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FmPlayAuthUserService extends AbstractUserService {
	private final UserHelper userHelper;

	@Inject
	public FmPlayAuthUserService(final PlayAuthenticate auth, UserHelper userHelper) {
		super(auth);

		this.userHelper = userHelper;
	}

	@Override
	public Object save(final AuthUser authUser) {
        GoogleAuthUser googleAuthUser = AuthUserConverter.toGoogleAuthUser(authUser);
        if (googleAuthUser == null) {
            return null;
        }

        User user = User.createUser(googleAuthUser.getId(), googleAuthUser.getEmail());

		if (!user.existsInDb()) {
			user.save();
			return user.getUserId();
		} else {
			return null;
		}
	}

	@Override
    @Transactional
	public Object getLocalIdentity(final AuthUserIdentity identity) {
		// For production: Caching might be a good idea here...
		// ...and dont forget to sync the cache when users get deactivated/deleted

        final User user = userHelper.findById(identity.getId());
        if(user != null) {
			return user.getUserId();
		} else {
			return null;
		}
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
