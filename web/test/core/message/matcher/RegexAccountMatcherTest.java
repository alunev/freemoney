package core.message.matcher;

import com.google.common.collect.Sets;
import dao.AccountDao;
import model.Account;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


/**
 * @author red
 * @since 0.0.1
 */
@RunWith(MockitoJUnitRunner.class)
public class RegexAccountMatcherTest {

    @Mock
    private AccountDao accountDao;

    @Test
    public void findsRegexPatternMatch() {
        Account account = Account.createAccount(
                "account01",
                "user01",
                "1111",
                "test RUB account",
                Currency.getInstance("RUB"),
                BigDecimal.valueOf(50.0),
                "2222"
        );

        when(accountDao.findByOwnerId("user01")).thenReturn(Sets.newHashSet(account));

        Optional<Account> bestMatch = new RegexAccountMatcher(accountDao).getBestMatch("user01", "2222");

        assertThat(bestMatch).isPresent();
        assertThat(bestMatch.get().getId()).isEqualTo("account01");
    }
}