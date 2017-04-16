package dao;

import com.google.inject.Inject;
import model.Account;
import model.Transaction;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.persistence.Query;
import java.util.List;


public class TransactionDao {
    private final JPAApi jpaApi;

    private final AccountDao accountDao;
    private final TransactionCategoryDao categoryDao;

    @Inject
    public TransactionDao(JPAApi jpaApi, AccountDao accountDao, TransactionCategoryDao categoryDao) {
        this.jpaApi = jpaApi;
        this.accountDao = accountDao;
        this.categoryDao = categoryDao;
    }

    public Transaction findById(String transactionId) {
        return jpaApi.withTransaction(
                em -> {
                    Transaction transaction = em.find(Transaction.class, transactionId);

                    if (transaction != null) {
                        transaction.setSourceAccount(accountDao.findById(transaction.getSourceId()));
                        transaction.setDestAccount(accountDao.findById(transaction.getDestId()));
                        transaction.setCategory(categoryDao.findById(transaction.getCategoryId()));
                    }

                    return transaction;
                }
        );
    }

    @Transactional
    public List<Transaction> findByOwnerId(String userId) {
        return jpaApi.withTransaction(
                em -> {
                    Query query = em.createQuery(String.format(
                            "Select t from Transaction t where t.ownerId = '%s'", userId
                    ));

                    return query.getResultList();
                }
        );
    }

    public void save(Transaction transaction) {
        jpaApi.<Void>withTransaction(em -> {
            em.persist(transaction);

            accountDao.save(transaction.getSourceAccount());
            accountDao.save(transaction.getDestAccount());
            categoryDao.save(transaction.getCategory());

            return null;
        });
    }

    public void delete(Transaction transaction) {
        jpaApi.<Void>withTransaction(em -> {
            em.remove(transaction);

            accountDao.delete(transaction.getSourceAccount());
            accountDao.delete(transaction.getDestAccount());
            categoryDao.delete(transaction.getCategory());

            return null;
        });
    }
}
