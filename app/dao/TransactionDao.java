package dao;

import com.google.inject.Inject;
import model.Account;
import model.Transaction;
import model.User;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import java.util.Collections;
import java.util.List;


public class TransactionDao {
    private final JPAApi jpaApi;

    private final AccountDao accountDao;

    @Inject
    public TransactionDao(JPAApi jpaApi, AccountDao accountDao) {
        this.jpaApi = jpaApi;
        this.accountDao = accountDao;
    }

    public Transaction findById(String transactionId) {
        return jpaApi.withTransaction(
                em -> {
                    Transaction transaction = em.find(Transaction.class, transactionId);

                    if (transaction != null) {
                        transaction.setSourceAccount(accountDao.findById(transaction.getSourceId()));
                        transaction.setDestAccount(accountDao.findById(transaction.getDestId()));
                    }

                    return transaction;
                }
        );
    }

    public void save(Transaction transaction) {
        jpaApi.<Void>withTransaction(em -> {
            em.persist(transaction);

            return null;
        });
    }

    public void delete(Transaction transaction) {
        jpaApi.<Void>withTransaction(em -> {
            em.remove(transaction);

            return null;
        });
    }
}
