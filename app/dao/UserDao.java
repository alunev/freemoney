package dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Account;
import model.Transaction;
import model.User;
import org.bson.types.ObjectId;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.Set;


@Singleton
public class UserDao {
    private final AccountDao accountDao;

    private final TransactionDao transactionDao;
    private PlayJongo playJongo;

    @Inject
    public UserDao(PlayJongo playJongo, AccountDao accountDao, TransactionDao transactionDao) {
        this.playJongo = playJongo;
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
        new User(user.getEmail(), user.getAccounts(), user.getTransactions());


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
        users().remove(new ObjectId(user.getId()));
    }

    public boolean idExistsInDb(String userId) {
        return users().findOne(userId).as(User.class) != null;
    }

    private org.jongo.MongoCollection users() {
        return playJongo.getCollection("users");
    }
}
