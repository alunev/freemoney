package dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.Sms;
import model.Transaction;
import org.bson.types.ObjectId;
import play.db.jpa.JPAApi;
import uk.co.panaxiom.playjongo.PlayJongo;

import javax.persistence.Query;
import java.math.BigInteger;


public class SmsDao {
    private PlayJongo playJongo;

    @Inject
    public SmsDao(PlayJongo playJongo) {
        this.playJongo = playJongo;
    }

    public Sms findById(String id) {
        return smses().findOne("{_id: #}", new ObjectId(id)).as(Sms.class);
    }

    public void save(Sms sms) {
        smses().save(sms);
    }

    public void delete(Sms sms) {
        smses().remove(new ObjectId(sms.getId()));
    }

    private org.jongo.MongoCollection smses() {
        return playJongo.getCollection("smses");
    }

}
