package core.message.matcher;

import model.Account;

import java.util.Optional;

/**
 * @author red
 * @since 0.0.1
 */
public interface AccountMatcher {
    Optional<Account> getBestMatch(String ownerId, String sourceString);
}
