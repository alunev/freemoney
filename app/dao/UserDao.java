package dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Account;
import model.Transaction;
import model.User;
import play.db.jpa.JPAApi;

import java.util.List;


@Singleton
public class UserDao {
    private final JPAApi jpaApi;

    private final AccountDao accountDao;

    private final TransactionDao transactionDao;

    @Inject
    public UserDao(JPAApi jpaApi, AccountDao accountDao, TransactionDao transactionDao) {
        this.jpaApi = jpaApi;
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    public User findById(String userId) {
        return jpaApi.withTransaction(
                em -> {
                    User user = em.find(User.class, userId);

                    if (user != null) {
                        user.setAccounts(accountDao.findByOwnerId(userId));
                        user.setTransactions(transactionDao.findByOwnerId(userId));
                    }

                    return user;
                }
        );
    }

    public void save(User user) {
        jpaApi.<Void>withTransaction(em -> {
            em.persist(user);

            updateAccounts(user);
            updateTransactions(user);

            return null;
        });
    }

    private void updateAccounts(User user) {
        List<Account> newAccounts = user.getAccounts();
        List<Account> oldAccounts = accountDao.findByOwnerId(user.getUserId());

        newAccounts.forEach(a -> a.setOwnerId(user.getUserId()));

        // save existing accounts
        accountDao.saveAll(newAccounts);

        // delete old but deleted accounts
        oldAccounts.removeAll(newAccounts);
        accountDao.deleteAll(oldAccounts);
    }

    private void updateTransactions(User user) {
        List<Transaction> newTransactions = user.getTransactions();
        List<Transaction> oldTransactions = transactionDao.findByOwnerId(user.getUserId());

        newTransactions.forEach(t -> t.setOwnerId(user.getUserId()));

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
}
