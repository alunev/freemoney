package core.message;

import dao.MessagePatternDao;
import model.MessagePattern;
import org.assertj.core.util.Maps;

import javax.inject.Inject;
import java.util.Map;
import java.util.Set;

/**
 * @author red
 * @since 0.0.1
 */
public class ParserFactory {
    private final MessagePatternDao messagePatternDao;

    private final static Map<String, RegexMessageParserFactory> parsers = Maps.newHashMap(
            "Tinkoff", new RegexMessageParserFactory()
    );

    @Inject
    public ParserFactory(MessagePatternDao messagePatternDao) {
        this.messagePatternDao = messagePatternDao;
    }

    public void createParserForBank(String bankName) {
        Set<MessagePattern> patterns = messagePatternDao.findByBankName(bankName);

        parsers.getOrDefault(bankName, new RegexMessageParserFactory()).createParser(patterns);
    }
}
