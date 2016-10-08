package auth;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.service.AbstractUserService;
import com.feth.play.module.pa.user.AuthUser;
import com.feth.play.module.pa.user.AuthUserIdentity;
import model.User;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MyUserService extends AbstractUserService {

	@Inject
	public MyUserService(final PlayAuthenticate auth) {
		super(auth);
	}

	@Override
	public Object save(final AuthUser authUser) {
//		final boolean isLinked = User.existsByAuthUserIdentity(authUser);
//		if (!isLinked) {
//			return User.create(authUser).id;
//		} else {
//			// we have this user already, so return null
//			return null;
//		}

		return new User();
	}

	@Override
	public Object getLocalIdentity(final AuthUserIdentity identity) {
		// For production: Caching might be a good idea here...
		// ...and dont forget to sync the cache when users get deactivated/deleted
//		final User u = User.findByAuthUserIdentity(identity);
//		if(u != null) {
//			return u.id;
//		} else {
//			return null;
//		}

		return "new_id_" + Math.random();
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
