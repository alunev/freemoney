package dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.WriteResult;
import model.Account;
import model.Transaction;
import model.User;
import org.bson.types.ObjectId;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.Set;
import java.util.stream.Collectors;


@Singleton
public class UserDao {
    private PlayJongo playJongo;

    private final AccountDao accountDao;

    private final TransactionDao transactionDao;

    @Inject
    public UserDao(PlayJongo playJongo, AccountDao accountDao, TransactionDao transactionDao) {
        this.playJongo = playJongo;
        this.accountDao = accountDao;
        this.transactionDao = transactionDao;
    }

    public User findById(String id) {
        User user = users().findOne("{_id: #}", new ObjectId(id)).as(User.class);

        return resolveReferences(user);
    }

    public User findByAuthId(String authId) {
        User user = users().findOne("{authId: #}", authId).as(User.class);

        resolveReferences(user);

        return user;
    }

    public String save(User user) {
        WriteResult result = users().save(user);

        saveAccounts(user);

        return result.isUpdateOfExisting() ? user.getId() : result.getUpsertedId().toString();
    }

    private User resolveReferences(User user) {
        return User.builder(user)
                   .withAccounts(accountDao.findByOwnerId(user.getId()))
                   .withTransactions(transactionDao.findByOwnerId(user.getId()))
                   .build();
    }

    private void saveAccounts(User user) {
        User.createUser(user.getAuthId(), user.getEmail(), user.getAccounts(), user.getTransactions());

        Set<Account> newAccounts = user.getAccounts()
                                       .stream()
                                       .map(a -> Account.copyWithOwnerId(a, user.getId()))
                                       .collect(Collectors.toSet());

        Set<Account> oldAccounts = accountDao.findByOwnerId(user.getId());

        // save all new accounts
        accountDao.saveAll(newAccounts);

        // delete old but deleted accounts
        oldAccounts.removeAll(newAccounts);
        accountDao.deleteAll(oldAccounts);
    }

    private void updateTransactions(User user) {
        Set<Transaction> newTransactions = user.getTransactions()
                                               .stream()
                                               .map(a -> Transaction.copyWithOwnerId(a, user.getId()))
                                               .collect(Collectors.toSet());

        Set<Transaction> oldTransactions = transactionDao.findByOwnerId(user.getId());

        // save all ne tx
        transactionDao.saveAll(newTransactions);

        // delete old but deleted tx
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
