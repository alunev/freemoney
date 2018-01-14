package core.message;

import model.MessagePattern;
import model.Sms;

import java.util.Collection;

/**
 * @author red
 * @since 0.0.1
 */
public class RegexMessageMatcher {

    private final Collection<MessagePattern> candidates;

    public RegexMessageMatcher(Collection<MessagePattern> candidates) {
        this.candidates = candidates;
    }


    public MessagePattern getBestMatch(Sms sms) {
        return null;
    }
}
