package core.message.matcher;

import dao.AccountDao;
import model.Account;

import javax.inject.Inject;
import java.util.Optional;

/**
 * @author red
 * @since 0.0.1
 */
public class RegexAccountMatcher implements AccountMatcher {
    private final AccountDao accountDao;

    @Inject
    public RegexAccountMatcher(AccountDao accountDao) {
        this.accountDao = accountDao;
    }

    @Override
    public Optional<Account> getBestMatch(String ownerId, String sourceString) {
        return accountDao.findByOwnerId(ownerId).stream()
                .filter(account -> account.getSmsPattern().equals(sourceString))
                .findFirst();
    }
}
