package core.message;

import model.MessagePattern;
import model.Sms;

import java.util.Collection;

/**
 * @author red
 * @since 0.0.1
 */
public class RegexBankMatcher implements BankMatcher {

    private final Collection<MessagePattern> candidates;

    public RegexBankMatcher(Collection<MessagePattern> candidates) {
        this.candidates = candidates;
    }


    @Override
    public String getBestMatch(Sms sms) {
        return null;
    }
}
