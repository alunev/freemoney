package auth;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.providers.oauth2.google.GoogleAuthInfo;
import com.feth.play.module.pa.providers.oauth2.google.GoogleAuthUser;
import dao.UserDao;
import org.junit.Test;
import play.libs.Json;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * @author red
 * @since 0.0.1
 */
public class FmPlayAuthUserServiceTest {

    @Test
    public void saveCreatesNewUser() {
        UserDao userDao = mock(UserDao.class);

        FmPlayAuthUserService service = new FmPlayAuthUserService(mock(PlayAuthenticate.class), userDao);

        service.save(new GoogleAuthUser(
                Json.parse("{\"id\" : 1234}"),
                new GoogleAuthInfo(Json.parse("{\"token_type\" : \"Bearer\", \"id_token\" : \"1234\"}")),
                ""
        ));

        verify(userDao, times(1)).save(any());
    }
}