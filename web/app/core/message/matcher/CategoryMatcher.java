package core.message.matcher;

import model.Account;
import model.TransactionCategory;

/**
 * @author red
 * @since 0.0.1
 */
public interface CategoryMatcher {

    TransactionCategory getBestMatch(String payeeString);

}
