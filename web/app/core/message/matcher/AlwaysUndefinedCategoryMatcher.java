package core.message.matcher;

import com.google.inject.Inject;
import dao.TransactionCategoryDao;
import model.TransactionCategory;

/**
 * @author red
 * @since 0.0.1
 */
public class AlwaysUndefinedCategoryMatcher implements CategoryMatcher {

    @Override
    public TransactionCategory getBestMatch(String payeeString) {
        return TransactionCategory.UNDEFINED;
    }
}
