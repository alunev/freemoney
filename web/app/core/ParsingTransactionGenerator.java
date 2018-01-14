package core;

import core.message.ParserFactory;
import core.message.RegexMessageMatcher;
import dao.MessagePatternDao;
import model.MessagePattern;
import model.Sms;
import model.Transaction;
import model.User;

import javax.inject.Inject;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author red
 * @since 0.0.1
 */
public class ParsingTransactionGenerator implements TransactionGenerator {
    private final MessagePatternDao messagePatternDao;
    private final ParserFactory parserFactory;

    @Inject
    public ParsingTransactionGenerator(MessagePatternDao messagePatternDao, ParserFactory parserFactory) {
        this.messagePatternDao = messagePatternDao;
        this.parserFactory = parserFactory;
    }

    @Override
    public Collection<Transaction> generate(Sms sms, User user) {
        Set<MessagePattern> patterns = messagePatternDao.findByOwnerId(user.getId());
        MessagePattern pattern = new RegexMessageMatcher(patterns).getBestMatch(sms);

        parserFactory.createParserForBank(pattern.getBankName());

        return Collections.emptyList();
    }
}
