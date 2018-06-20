package dao;

import com.google.common.base.Strings;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.WriteResult;
import model.Account;
import model.AppInstance;
import model.Transaction;
import model.User;
import org.assertj.core.util.Lists;
import org.bson.types.ObjectId;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        if (Strings.isNullOrEmpty(id)) {
            throw new RuntimeException("id is empty");
        }

        User user = users().findOne("{_id: #}", new ObjectId(id)).as(User.class);

        if (user == null) {
            return null;
        }

        return resolveReferences(user);
    }

    public Optional<User> findByAuthId(String authId) {
        return Optional.ofNullable(users().findOne("{authId: #}", authId).as(User.class))
                .map(this::resolveReferences);
    }

    public String save(User user) {
        WriteResult result = users().save(user);

        saveAccounts(user);

        return result.isUpdateOfExisting() ? user.get_id() : result.getUpsertedId().toString();
    }

    private User resolveReferences(User user) {
        return User.builder(user)
                   .withAccounts(accountDao.findByOwnerId(user.get_id()))
                   .withTransactions(transactionDao.findByOwnerId(user.get_id()))
                   .build();
    }

    private void saveAccounts(User user) {
        Set<Account> newAccounts = user.getAccounts()
                                       .stream()
                                       .map(a -> Account.copyWithOwnerId(a, user.get_id()))
                                       .collect(Collectors.toSet());

        Set<Account> oldAccounts = accountDao.findByOwnerId(user.get_id());

        // save all new accounts
        accountDao.saveAll(newAccounts);

        // delete old but deleted accounts
        oldAccounts.removeAll(newAccounts);
        accountDao.deleteAll(oldAccounts);
    }

    private void updateTransactions(User user) {
        Set<Transaction> newTransactions = user.getTransactions()
                                               .stream()
                                               .map(a -> Transaction.copyWithOwnerId(a, user.get_id()))
                                               .collect(Collectors.toSet());

        Set<Transaction> oldTransactions = transactionDao.findByOwnerId(user.get_id());

        // save all ne tx
        transactionDao.saveAll(newTransactions);

        // delete old but deleted tx
        oldTransactions.removeAll(newTransactions);
        transactionDao.deleteAll(oldTransactions);
    }

    public void delete(User user) {
        users().remove(new ObjectId(user.get_id()));
    }

    public User updateInstanceLastSyncTs(User user, String deviceId, ZonedDateTime dateTime) {
        AppInstance appInstance = new AppInstance(deviceId, dateTime);

        List<AppInstance> userInstances = Optional.ofNullable(user.getAppInstances())
                .orElse(Lists.emptyList());
        userInstances = userInstances.stream()
                .filter(instance -> Objects.equals(instance.getInstanceId(), appInstance.getInstanceId()))
                .collect(Collectors.toList());
        userInstances.add(appInstance);

        User updatedUser = User.builder(user)
                .withAppInstances(userInstances)
                .build();

        this.save(updatedUser);

        return updatedUser;
    }

    private org.jongo.MongoCollection users() {
        return playJongo.getCollection("users");
    }
}
