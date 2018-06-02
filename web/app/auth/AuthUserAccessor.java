package auth;

import com.feth.play.module.pa.providers.oauth2.google.GoogleAuthUser;
import com.feth.play.module.pa.providers.password.UsernamePasswordAuthUser;
import com.feth.play.module.pa.user.AuthUser;

/**
 * Created by admin on 10/26/2016.
 */
public class AuthUserAccessor {

    public static String getEmailOf(AuthUser authUser) {
        if ("google".equals(authUser.getProvider())) {
            return ((GoogleAuthUser) authUser).getEmail();
        } else if ("basic".equals(authUser.getProvider())) {
            return ((UsernamePasswordAuthUser) authUser).getEmail();
        } else {
            return null;
        }
    }

    private static GoogleAuthUser toGoogleAuthUser(AuthUser authUser) {
        if ("google".equals(authUser.getProvider())) {
            return (GoogleAuthUser) authUser;
        } else {
            return null;
        }
    }
}
