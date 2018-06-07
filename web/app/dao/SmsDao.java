package dao;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import model.Sms;
import org.bson.types.ObjectId;
import org.jongo.MongoCursor;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


public class SmsDao {
    private PlayJongo playJongo;

    @Inject
    public SmsDao(PlayJongo playJongo) {
        this.playJongo = playJongo;
    }

    public Sms findById(String id) {
        return smses().findOne("{_id: #}", new ObjectId(id)).as(Sms.class);
    }

    public Set<Sms> findByOwnerId(String userId) {
        MongoCursor<Sms> mongoCursor = smses().find("{ownerId: #}", userId).as(Sms.class);

        return new HashSet<>(Sets.newHashSet(mongoCursor.iterator()));
    }

    public Sms save(Sms sms) {
        smses().save(sms);

        return sms;
    }

    public void delete(Sms sms) {
        smses().remove(new ObjectId(sms.getId()));
    }

    private org.jongo.MongoCollection smses() {
        return playJongo.getCollection("smses");
    }

}
