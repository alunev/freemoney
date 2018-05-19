package core.message;

import model.MessagePattern;
import model.Sms;

import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * @author red
 * @since 0.0.1
 */
public class RegexBankMatcher implements BankMatcher {

    private final Collection<MessagePattern> patterns;

    public RegexBankMatcher(Collection<MessagePattern> patterns) {
        this.patterns = patterns;
    }

    @Override
    public Optional<String> getBestMatch(Sms sms) {
        return patterns.stream()
                .filter(p -> Pattern.matches(p.getRegex(), sms.getText()))
                .findFirst()
                .map(MessagePattern::getBankName);
    }
}
