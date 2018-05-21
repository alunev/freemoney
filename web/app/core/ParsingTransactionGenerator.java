package core;

import core.message.ParserSelector;
import core.message.RegexBankMatcher;
import core.message.matcher.AccountMatcher;
import core.message.matcher.CategoryMatcher;
import core.message.parser.MessageParser;
import core.message.parser.ParseResult;
import dao.MessagePatternDao;
import model.Account;
import model.MessagePattern;
import model.Sms;
import model.Transaction;
import model.User;
import org.assertj.core.util.Lists;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author red
 * @since 0.0.1
 */
public class ParsingTransactionGenerator implements TransactionGenerator {
    private final MessagePatternDao messagePatternDao;
    private final ParserSelector parserSelector;
    private final AccountMatcher accountMatcher;
    private final CategoryMatcher categoryMatcher;

    @Inject
    public ParsingTransactionGenerator(MessagePatternDao messagePatternDao,
                                       ParserSelector parserSelector,
                                       AccountMatcher accountMatcher,
                                       CategoryMatcher categoryMatcher) {
        this.messagePatternDao = messagePatternDao;
        this.parserSelector = parserSelector;
        this.accountMatcher = accountMatcher;
        this.categoryMatcher = categoryMatcher;
    }

    @Override
    public List<Transaction> generate(Sms sms, User user) {
        Set<MessagePattern> userPatterns = messagePatternDao.findByOwnerId(user.getId());
        Optional<String> bankName = new RegexBankMatcher(userPatterns).getBestMatch(sms);

        if (!bankName.isPresent()) {
            return Collections.emptyList();
        }

        Set<MessagePattern> bankPatterns = userPatterns.stream()
                .filter(messagePattern -> messagePattern.getBankName().equals(bankName.get()))
                .collect(Collectors.toSet());

        MessageParser parser = parserSelector.getParserForBank(bankName.get());
        ParseResult result = parser.parse(sms, bankPatterns);

        Transaction.TransactionBuilder builder = Transaction.builder();
        builder.ownerId(user.getId());
        builder.transactionType(result.getTransactionType());
        builder.sourceAmount(result.getAmount());
        builder.destAmount(result.getAmount());
        builder.categoryId(categoryMatcher.getBestMatch(result.getPayeeString()).getId());

        switch (result.getTransactionType()) {
            case EXPENSE:
                Account sourceAccount = accountMatcher.getBestMatch(result.getSourceString());
                builder.sourceId(sourceAccount.getId());
                builder.destId(Account.EXPENSE_ACCOUNT.getId());
            break;
        }

        return Lists.newArrayList(builder.build());
    }
}
