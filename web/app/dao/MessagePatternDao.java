package dao;

import com.google.common.collect.Sets;
import model.MessagePattern;
import org.jongo.MongoCursor;
import uk.co.panaxiom.playjongo.PlayJongo;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Set;

/**
 * @author red
 * @since 0.0.1
 */

@Singleton
public class MessagePatternDao {
    private final PlayJongo playJongo;

    @Inject
    public MessagePatternDao(PlayJongo playJongo) {
        this.playJongo = playJongo;
    }

    public Set<MessagePattern> findByOwnerId(String userId) {
        MongoCursor<MessagePattern> mongoCursor = patterns().find("{ownerId: #}", userId).as(MessagePattern.class);

        return Sets.newHashSet(mongoCursor.iterator());
    }

    public Set<MessagePattern> findByBankName(String bankName) {
        MongoCursor<MessagePattern> mongoCursor = patterns().find("{bankName: #}", bankName).as(MessagePattern.class);

        return Sets.newHashSet(mongoCursor.iterator());
    }

    private org.jongo.MongoCollection patterns() {
        return playJongo.getCollection("patterns");
    }

}
