package core;

import model.Sms;
import model.Transaction;
import model.User;

import java.util.List;

/**
 * @author red
 * @since 0.0.1
 */
public interface TransactionGenerator {
    List<Transaction> generate(Sms sms, User user);
}
