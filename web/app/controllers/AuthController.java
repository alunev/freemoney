package controllers;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import model.User;
import play.mvc.Controller;
import play.mvc.Result;
import services.UserService;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

/**
 * @author red
 * @since 0.0.1
 */
public class AuthController extends Controller {


    private final UserService userService;

    private final Config config;

    @Inject
    public AuthController(UserService userService, Config config) {
        this.userService = userService;
        this.config = config;
    }

    public Result signOut() {
        session().clear();

        return ok();
    }

    public Result tokensignin() {
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = new JacksonFactory();

        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(config.getString("google.clientId")))
                .build();

        Optional<String> idTokenString = Optional.ofNullable(request().body().asFormUrlEncoded().get("idtoken"))
                .map(strings -> strings[0]);

        if (!idTokenString.isPresent()) {
            return badRequest("No idtoken found");
        }

        GoogleIdToken idToken = null;
        try {
            idToken = verifier.verify(idTokenString.get());
        } catch (GeneralSecurityException | IOException e) {
            return internalServerError(e.toString());
        }

        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Print user identifier
            String userId = payload.getSubject();
            System.out.println("User ID: " + userId);

            // Get profile information from payload
            String email = payload.getEmail();
            boolean emailVerified = Boolean.valueOf(payload.getEmailVerified());
            String name = (String) payload.get("name");
            String pictureUrl = (String) payload.get("picture");
            String locale = (String) payload.get("locale");
            String familyName = (String) payload.get("family_name");
            String givenName = (String) payload.get("given_name");

            idToken.getPayload();

            // Use or store profile information
            // ...

            Optional<User> user = userService.getOrCreateUser(userId, email);

            session("userId", userId);

            return ok();
        } else {
            System.out.println("Invalid ID token.");
            return ok();
        }
    }
}
