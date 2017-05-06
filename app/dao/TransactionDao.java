package dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Transaction;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.persistence.Query;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
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
                        updateTransientFields(transaction);
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

                    return ((List<Transaction>) query.getResultList()).stream().map(t -> {
                        updateTransientFields(t);

                        return t;
                    }).collect(Collectors.toList());
                }
        );
    }

    public void save(Transaction transaction) {
        jpaApi.<Void>withTransaction(em -> {
            em.persist(transaction);

            saveTransientFields(transaction);

            return null;
        });
    }

    public void delete(Transaction transaction) {
        jpaApi.<Void>withTransaction(em -> {
            em.remove(transaction);

            deleteTransientFields(transaction);

            return null;
        });
    }

    public void saveAll(List<Transaction> transactions) {
        jpaApi.<Void>withTransaction(em -> {
            for (Transaction tx : transactions) {
                em.persist(tx);

                saveTransientFields(tx);
            }

            return null;
        });
    }

    public void deleteAll(List<Transaction> transactions) {
        if (transactions.isEmpty()) {
            return;
        }

        jpaApi.<Void>withTransaction(em -> {
            for (Transaction tx : transactions) {
                em.remove(tx);
            }

            return null;
        });
    }

    private void saveTransientFields(Transaction transaction) {
        categoryDao.save(transaction.getCategory());
        accountDao.save(transaction.getSourceAccount());
        accountDao.save(transaction.getDestAccount());
    }

    private void deleteTransientFields(Transaction transaction) {
        categoryDao.delete(transaction.getCategory());
        accountDao.delete(transaction.getSourceAccount());
        accountDao.delete(transaction.getDestAccount());
    }

    private void updateTransientFields(Transaction transaction) {
        transaction.setCategory(categoryDao.findById(transaction.getCategoryId()));
        transaction.setSourceAccount(accountDao.findById(transaction.getSourceId()));
        transaction.setDestAccount(accountDao.findById(transaction.getDestId()));
    }
}
