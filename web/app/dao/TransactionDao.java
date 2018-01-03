package dao;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Account;
import model.Transaction;
import org.bson.types.ObjectId;
import org.jongo.MongoCursor;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;
import uk.co.panaxiom.playjongo.PlayJongo;

import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class TransactionDao {
    private PlayJongo playJongo;

    private final AccountDao accountDao;
    private final TransactionCategoryDao categoryDao;

    @Inject
    public TransactionDao(PlayJongo playJongo, AccountDao accountDao, TransactionCategoryDao categoryDao) {
        this.playJongo = playJongo;
        this.accountDao = accountDao;
        this.categoryDao = categoryDao;
    }

    public Transaction findById(String transactionId) {
        Transaction transaction = transactions().findOne("{_id: #}", new ObjectId(transactionId)).as(Transaction.class);

        if (transaction == null) {
            return null;
        }

        return updateTransientFields(transaction);
    }

    public Set<Transaction> findByOwnerId(String userId) {
        MongoCursor<Transaction> mongoCursor = transactions().find("{ownerId: #}", userId).as(Transaction.class);

        return Sets.newHashSet(mongoCursor.iterator()).stream()
                .map(this::updateTransientFields)
                .collect(Collectors.toSet());
    }

    public void save(Transaction transaction) {
        transactions().save(transaction);
    }

    public void delete(Transaction transaction) {
        transactions().remove(new ObjectId(transaction.getId()));
    }

    public void saveAll(Set<Transaction> transactions) {
        transactions.forEach(this::save);
    }

    public void deleteAll(Set<Transaction> transactions) {
        transactions.forEach(this::delete);
    }

    private Transaction updateTransientFields(Transaction transaction) {
        transaction.setCategory(categoryDao.findById(transaction.getCategoryId()));
        transaction.setSourceAccount(accountDao.findById(transaction.getSourceId()));
        transaction.setDestAccount(accountDao.findById(transaction.getDestId()));

        return transaction;
    }

    private org.jongo.MongoCollection transactions() {
        return playJongo.getCollection("transactions");
    }
}
