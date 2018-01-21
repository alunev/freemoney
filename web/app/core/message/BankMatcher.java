package core.message;

import model.Sms;

/**
 * @author red
 * @since 0.0.1
 */
public interface BankMatcher {
    String getBestMatch(Sms sms);
}
