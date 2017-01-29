package dao;

import com.google.inject.Inject;
import model.Account;
import model.User;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import java.util.Collections;
import java.util.List;


public class UserDao {
    private final JPAApi jpaApi;

    private final AccountDao accountDao;

    @Inject
    public UserDao(JPAApi jpaApi, AccountDao accountDao) {
        this.jpaApi = jpaApi;
        this.accountDao = accountDao;
    }

    public User findById(String userId) {
        return jpaApi.withTransaction(
                em -> {
                    User user = em.find(User.class, userId);

                    if (user != null) {
                        user.setAccounts(accountDao.findByOwnerId(userId));
                    }

                    return user;
                }
        );
    }

    public void save(User user) {
        jpaApi.<Void>withTransaction(em -> {
            em.persist(user);
            List<Account> newAccounts = user.getAccounts();
            List<Account> oldAccounts = accountDao.findByOwnerId(user.getUserId());

            // save existing accounts
            accountDao.saveAll(newAccounts);

            // delete old but deleted accounts
            oldAccounts.removeAll(newAccounts);
            accountDao.deleteAll(oldAccounts);

            return null;
        });
    }

    public void delete(User user) {
        jpaApi.<Void>withTransaction(em -> {
            em.remove(user);

            accountDao.deleteAll(user.getAccounts());

            return null;
        });
    }

    @Transactional
    public boolean idExistsInDb(String userId) {
        return jpaApi.withTransaction(em -> em.find(User.class, userId)) != null;
    }
}
