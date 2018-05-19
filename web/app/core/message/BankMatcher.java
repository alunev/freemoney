package core.message;

import model.Sms;

import java.util.Optional;

/**
 * @author red
 * @since 0.0.1
 */
public interface BankMatcher {
    Optional<String> getBestMatch(Sms sms);
}
