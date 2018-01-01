package dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.TransactionCategory;
import play.db.jpa.JPAApi;
import play.db.jpa.Transactional;

import javax.persistence.Query;
import java.util.List;

/**
 * Created by red on 30.12.16.
 */

@Singleton
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
    public List<TransactionCategory> findAll() {
        return jpaApi.withTransaction(
                em -> {
                    Query query = em.createQuery("Select c from TransactionCategory c");

                    return ((List<TransactionCategory>) query.getResultList());
                }
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
