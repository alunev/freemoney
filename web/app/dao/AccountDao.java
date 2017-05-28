package dao;

import com.google.inject.Inject;
import model.Account;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.persistence.Query;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by red on 30.12.16.
 */
public class AccountDao {

    private final JPAApi jpaApi;

    @Inject
    public AccountDao(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }


    @Transactional
    public Account findById(String accountId) {
        return jpaApi.withTransaction(
                em -> em.find(Account.class, accountId)
        );
    }

    @Transactional
    public Set<Account> findByOwnerId(String userId) {
        return jpaApi.withTransaction(
                em -> {
                    Query query = em.createQuery(String.format(
                            "Select a from Account a where a.ownerId = '%s'", userId
                    ));

                    return new HashSet<>(query.getResultList());
                }
        );
    }

    @Transactional
    public void save(Account account) {
        jpaApi.<Void>withTransaction(em -> {
            em.persist(account);
            return null;
        });
    }

    @Transactional
    public void saveAll(Set<Account> accounts) {
        jpaApi.<Void>withTransaction(em -> {
            for (Account account : accounts) {
                em.persist(account);
            }

            return null;
        });
    }

    @Transactional
    public void delete(Account account) {
        jpaApi.<Void>withTransaction(em -> {
            em.remove(account);
            return null;
        });
    }

    public void deleteAll(Set<Account> accounts) {
        if (accounts.isEmpty()) {
            return;
        }

        jpaApi.<Void>withTransaction(em -> {
            for (Account account : accounts) {
                em.remove(account);
            }

            return null;
        });
    }
}
