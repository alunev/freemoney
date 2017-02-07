package auth.deadbolt;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.ExecutionContextProvider;
import be.objectify.deadbolt.java.cache.HandlerCache;
import com.feth.play.module.pa.PlayAuthenticate;
import dao.UserDao;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class FmPlayAuthHandlerCache implements HandlerCache {

	private final DeadboltHandler defaultHandler;

	private final PlayAuthenticate auth;

	private final UserDao userDao;

	@Inject
	public FmPlayAuthHandlerCache(final PlayAuthenticate auth, final ExecutionContextProvider execContextProvider, UserDao userDao) {
		this.auth = auth;
		this.userDao = userDao;
		this.defaultHandler = new FmPlayAuthDeadboltHandler(auth, execContextProvider, userDao);
	}

	@Override
	public DeadboltHandler apply(final String key) {
		return this.defaultHandler;
	}

	@Override
	public DeadboltHandler get() {
		return this.defaultHandler;
	}
}