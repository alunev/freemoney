package dao;

import com.google.inject.Inject;
import model.Account;
import model.User;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.persistence.EntityManager;
import java.util.List;


public class UserDao {
    private final JPAApi jpaApi;

    @Inject
    public UserDao(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    @Transactional
    public User findById(String userId) {
        return jpaApi.withTransaction(
                em -> em.find(User.class, userId)
        );
    }

    @Transactional
    public void save(User user) {
        jpaApi.<Void>withTransaction(em -> {
            // kundera fails to do cascade delete/update on redis for some reason - so do it for it
            deleteRelatedObjectsIfChanged(user, em);

            em.flush();

            em.persist(user);

            return null;
        });
    }

    private void deleteRelatedObjectsIfChanged(User user, EntityManager em) {
        User existingUser = findById(user.getUserId());

        if (existingUser != null) {
            if (accountsChanged(user, existingUser)) {
                deleteAccounts(em, existingUser.getAccounts());
            }
        }
    }

    private boolean accountsChanged(User newUser, User existingUser) {
        List<Account> oldAccounts = existingUser.getAccounts();
        List<Account> newAccounts = newUser.getAccounts();

        if (oldAccounts == null || newAccounts == null) {
            return !(oldAccounts == null && newAccounts == null);
        }

        if (oldAccounts.size() != newAccounts.size()) {
            return true;
        }

        for (int i = 0; i < oldAccounts.size(); i++) {
            if (!oldAccounts.get(i).isSame(newAccounts.get(i))) {
                return true;
            }
        }

        return false;
    }

    @Transactional
    public void merge(User user) {
        jpaApi.<Void>withTransaction(em -> {
            em.merge(user);
            return null;
        });
    }

    @Transactional
    public void delete(User user) {
        jpaApi.<Void>withTransaction(em -> {
            em.remove(user);
            deleteAccounts(em, user.getAccounts());
            return null;
        });
    }

    @Transactional
    public boolean idExistsInDb(String userId) {
        return jpaApi.withTransaction(em -> em.find(User.class, userId)) != null;
    }

    private void deleteAccounts(EntityManager em, List<Account> accounts) {
        if (accounts == null) {
            return;
        }

        for (Account account : accounts) {
            em.remove(account);
        }
    }
}
