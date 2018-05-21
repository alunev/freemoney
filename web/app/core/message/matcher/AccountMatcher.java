package core.message.matcher;

import model.Account;

/**
 * @author red
 * @since 0.0.1
 */
public interface AccountMatcher {
    Account getBestMatch(String sourceString);
}
