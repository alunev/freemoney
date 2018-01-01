package dao;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import model.Account;
import org.bson.types.ObjectId;
import org.jongo.MongoCursor;
import play.db.jpa.Transactional;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.Set;

/**
 * Created by red on 30.12.16.
 */
public class AccountDao {

    private PlayJongo playJongo;

    @Inject
    public AccountDao(PlayJongo playJongo) {
        this.playJongo = playJongo;
    }

    public Account findById(String accountId) {
        return accounts().findOne("{_id: #}", new ObjectId(accountId)).as(Account.class);
    }

    public Set<Account> findByOwnerId(String userId) {
        MongoCursor<Account> mongoCursor = accounts().find("{ownerId: #}", userId).as(Account.class);

        return Sets.newHashSet(mongoCursor.iterator());
    }

    @Transactional
    public void save(Account account) {
        accounts().save(account);
    }

    @Transactional
    public void saveAll(Set<Account> accounts) {
        accounts.forEach(this::save);
    }

    @Transactional
    public void delete(Account account) {
        accounts().remove(new ObjectId(account.getId()));
    }

    public void deleteAll(Set<Account> accounts) {
        accounts.forEach(this::delete);
    }

    private org.jongo.MongoCollection accounts() {
        return playJongo.getCollection("accounts");
    }
}
