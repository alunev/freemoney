package core.message;

import core.message.parser.MessageParser;
import core.message.parser.RegexMessageParser;
import core.message.parser.TinkoffMessageParser;
import dao.MessagePatternDao;
import org.assertj.core.util.Maps;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Map;

/**
 * @author red
 * @since 0.0.1
 */

public class ParserSelector {

    private final static Map<String, MessageParser> parsers = Maps.newHashMap(
            "Tinkoff", new TinkoffMessageParser()
    );

    public MessageParser getParserForBank(String bankName) {
        return parsers.getOrDefault(bankName, new RegexMessageParser());
    }
}
