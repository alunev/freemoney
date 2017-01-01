package dao;

import com.google.inject.Inject;
import model.Account;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

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
    public void save(Account account) {
        jpaApi.<Void>withTransaction(em -> {
            em.persist(account);
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
}
