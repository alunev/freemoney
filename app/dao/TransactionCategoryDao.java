package dao;

import com.google.inject.Inject;
import model.TransactionCategory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

/**
 * Created by red on 30.12.16.
 */
public class TransactionCategoryDao {

    private final JPAApi jpaApi;

    @Inject
    public TransactionCategoryDao(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }


    @Transactional
    public TransactionCategory findById(String categoryId) {
        return jpaApi.withTransaction(
                em -> em.find(TransactionCategory.class, categoryId)
        );
    }

    @Transactional
    public void save(TransactionCategory category) {
        jpaApi.<Void>withTransaction(em -> {
            em.persist(category);
            return null;
        });
    }

    @Transactional
    public void delete(TransactionCategory category) {
        jpaApi.<Void>withTransaction(em -> {
            em.remove(category);
            return null;
        });
    }
}
