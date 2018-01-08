package dao;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import model.TransactionCategory;
import org.bson.types.ObjectId;
import org.jongo.MongoCursor;
import uk.co.panaxiom.playjongo.PlayJongo;

import java.util.Set;

/**
 * Created by red on 30.12.16.
 */

@Singleton
public class TransactionCategoryDao {

    private PlayJongo playJongo;

    @Inject
    public TransactionCategoryDao(PlayJongo playJongo) {
        this.playJongo = playJongo;
    }

    public TransactionCategory findById(String categoryId) {
        return categories().findOne("{_id: #}", new ObjectId(categoryId)).as(TransactionCategory.class);
    }

    public Set<TransactionCategory> findAll() {
        MongoCursor<TransactionCategory> mongoCursor = categories().find().as(TransactionCategory.class);

        return Sets.newHashSet(mongoCursor.iterator());
    }

    public void save(TransactionCategory category) {
        categories().save(category);
    }

    public void delete(TransactionCategory category) {
        categories().remove(new ObjectId(category.getId()));
    }

    private org.jongo.MongoCollection categories() {
        return playJongo.getCollection("categories");
    }
}
