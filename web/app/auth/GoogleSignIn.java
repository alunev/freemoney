package auth;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import model.User;
import play.Logger;
import services.UserService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

/**
 * @author red
 * @since 0.0.1
 */
public class GoogleSignIn {
    private final UserService userService;
    private final Config config;

    @Inject
    public GoogleSignIn(UserService userService, Config config) {
        this.userService = userService;
        this.config = config;
    }

    public Optional<User> processSignInToken(String idTokenString) throws GeneralSecurityException, IOException {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(config.getString("google.clientId")))
                .build();

        GoogleIdToken idToken = null;
        idToken = verifier.verify(idTokenString);

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            String userId = payload.getSubject();
            String email = payload.getEmail();

            Optional<User> user = userService.getOrCreateUser(userId, email);

            Logger.info(email + " logged in");

            return user;
        } else {
            System.out.println("Invalid ID token.");
            return Optional.empty();
        }
    }

}
