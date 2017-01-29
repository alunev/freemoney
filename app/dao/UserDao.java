package dao;

import com.google.inject.Inject;
import model.User;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;


public class UserDao {
    private final JPAApi jpaApi;

    @Inject
    public UserDao(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public User findById(String userId) {
        return jpaApi.withTransaction(
                em -> em.find(User.class, userId)
        );
    }

    public void save(User user) {
        jpaApi.<Void>withTransaction(em -> {
            em.persist(user);
            em.flush();

            return null;
        });
    }

    public void merge(User user) {
        jpaApi.<Void>withTransaction(em -> {
            em.merge(user);

            return null;
        });
    }

    public void delete(User user) {
        jpaApi.<Void>withTransaction(em -> {
            em.remove(user);

            return null;
        });
    }

    @Transactional
    public boolean idExistsInDb(String userId) {
        return jpaApi.withTransaction(em -> em.find(User.class, userId)) != null;
    }
}
