package core;

import core.message.ParserSelector;
import core.message.parser.AccountMatcher;
import core.message.parser.MessageParser;
import dao.MessagePatternDao;
import dao.ObjectsFactory;
import model.Account;
import model.Transaction;
import model.TransactionType;
import model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author red
 * @since 0.0.1
 */
@RunWith(MockitoJUnitRunner.class)
public class ParsingTransactionGeneratorTest {

    @Mock
    private MessagePatternDao messagePatternDao;

    @Mock
    private ParserSelector parserSelector;

    @Mock
    private AccountMatcher accountMatcher;

    @Test
    public void generatesSingleTransaction() {
        MessageParser messageParser = mock(MessageParser.class);

        when(messagePatternDao.findByOwnerId("user01")).thenReturn(Collections.singleton(ObjectsFactory.createMessagePattern()));
        when(parserSelector.getParserForBank("bank1")).thenReturn(messageParser);
        when(messageParser.parse(any(), any())).thenReturn(ObjectsFactory.createParseResult());
        when(accountMatcher.getBestMatch(any())).thenReturn(ObjectsFactory.createDummyAccountWithIdOwnerId("1", "user01"));

        List<Transaction> transactions = new ParsingTransactionGenerator(messagePatternDao, parserSelector, accountMatcher).generate(
                ObjectsFactory.sampleTfSms(),
                User.builder()
                        .with_id("user01")
                        .build()
        );

        assertThat(transactions).hasSize(1);
        assertThat(transactions.get(0).getTransactionType()).isEqualTo(TransactionType.EXPENSE);
        assertThat(transactions.get(0).getSourceAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(transactions.get(0).getDestAmount()).isEqualTo(BigDecimal.TEN);
        assertThat(transactions.get(0).getSourceId()).isEqualTo("1111");
        assertThat(transactions.get(0).getDestId()).isEqualTo(Account.EXPENSE_ACCOUNT.getId());
    }
}