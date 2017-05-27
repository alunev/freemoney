package dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Account;
import model.Transaction;
import model.User;
import org.jongo.Jongo;

import java.util.Set;


@Singleton
public class UserDao {
    private final AccountDao accountDao;

    private final TransactionDao transactionDao;
    private Jongo jongo;

    @Inject
    public UserDao(Jongo jongo, AccountDao accountDao, TransactionDao transactionDao) {
        this.jongo = jongo;
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    public User findById(String userId) {
        return users().findOne("{userId: #}", userId).as(User.class);
    }

    public void save(User user) {
        users().save(user);
    }

    private void updateAccounts(User user) {
        new User(user.getId(), user.getEmail(), user.getAccounts(), user.getTransactions());


        Set<Account> newAccounts = user.getAccounts();
        Set<Account> oldAccounts = accountDao.findByOwnerId(user.getId());

        newAccounts.forEach(a -> a.setOwnerId(user.getId()));

        // save existing accounts
        accountDao.saveAll(newAccounts);

        // delete old but deleted accounts
        oldAccounts.removeAll(newAccounts);
        accountDao.deleteAll(oldAccounts);
    }

    private void updateTransactions(User user) {
        Set<Transaction> newTransactions = user.getTransactions();
        Set<Transaction> oldTransactions = transactionDao.findByOwnerId(user.getId());

        newTransactions.forEach(t -> t.setOwnerId(user.getId()));

        // save existing accounts
        transactionDao.saveAll(newTransactions);

        // delete old but deleted accounts
        oldTransactions.removeAll(newTransactions);
        transactionDao.deleteAll(oldTransactions);
    }

    public void delete(User user) {
        jpaApi.<Void>withTransaction(em -> {
            em.remove(user);

            accountDao.deleteAll(user.getAccounts());

            return null;
        });
    }

    public boolean idExistsInDb(String userId) {
        return jpaApi.withTransaction(em -> em.find(User.class, userId)) != null;
    }

    private org.jongo.MongoCollection users() {
        return jongo.getCollection("users");
    }
}
