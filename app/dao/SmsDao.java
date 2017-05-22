package dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Account;
import model.Sms;
import model.Transaction;
import model.Sms;
import play.db.jpa.JPAApi;

import javax.persistence.Query;
import java.math.BigInteger;
import java.util.List;


@Singleton
public class SmsDao {
    public static final String ID_SEQ_NAME = "smsIdSeq";

    private final JPAApi jpaApi;

    @Inject
    public SmsDao(JPAApi jpaApi) {
        this.jpaApi = jpaApi;
    }

    public long nextId() {
        return jpaApi.withTransaction(
                em -> {
                    Query q = em.createNativeQuery("INCR " + ID_SEQ_NAME, Sms.class);
                    return ((BigInteger) q.getSingleResult()).longValue();
                }
        );
    }


    public Sms findById(String id) {
        return jpaApi.withTransaction(
                em -> {
                    Sms sms = em.find(Sms.class, id);
                    return sms;
                }
        );
    }

    public void save(Sms sms) {
        jpaApi.<Void>withTransaction(em -> {
            em.persist(sms);

            return null;
        });
    }

    public void delete(Sms sms) {
        jpaApi.<Void>withTransaction(em -> {
            em.remove(sms);

            return null;
        });
    }
}
