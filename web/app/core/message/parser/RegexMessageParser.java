package core.message.parser;

import model.MessagePattern;
import model.Sms;

import java.util.Collection;
import java.util.Optional;

/**
 * @author red
 * @since 0.0.1
 */
public class RegexMessageParser implements MessageParser {

    public Optional<ParseResult> parse(Sms sms, Collection<MessagePattern> patterns) {

        return Optional.ofNullable(ParseResult.builder().build());
    }

}
