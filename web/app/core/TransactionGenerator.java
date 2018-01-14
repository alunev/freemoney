package core;

import model.Sms;
import model.Transaction;
import model.User;

import java.util.Collection;

/**
 * @author red
 * @since 0.0.1
 */
public interface TransactionGenerator {
    Collection<Transaction> generate(Sms sms, User user);
}
