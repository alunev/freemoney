package core.message.parser;

import model.MessagePattern;
import model.Sms;

import java.util.Collection;

/**
 * @author red
 * @since 0.0.1
 */
public class RegexMessageParser implements MessageParser {

    public ParseResult parse(Sms sms, Collection<MessagePattern> patterns) {

        return ParseResult.builder().build();
    }

}
