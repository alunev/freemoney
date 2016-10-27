package auth;

import com.feth.play.module.pa.providers.oauth2.google.GoogleAuthUser;
import com.feth.play.module.pa.user.AuthUser;

/**
 * Created by admin on 10/26/2016.
 */
public class AuthUserConverter {
    public static final GoogleAuthUser toGoogleAuthUser(AuthUser authUser) {
        if ("google".equals(authUser.getProvider())) {
            return (GoogleAuthUser) authUser;
        } else {
            return null;
        }
    }
}
