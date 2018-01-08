package dao;

import com.google.inject.Inject;
import model.Sms;
import org.bson.types.ObjectId;
import uk.co.panaxiom.playjongo.PlayJongo;


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
