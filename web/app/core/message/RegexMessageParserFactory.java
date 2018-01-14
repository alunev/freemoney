package core.message;

import model.MessagePattern;

import java.util.Set;

/**
 * @author red
 * @since 0.0.1
 */
public class RegexMessageParserFactory {
    public MessageParser createParser(Set<MessagePattern> patterns) {
        return new RegexMessageParser(patterns);
    }
}
