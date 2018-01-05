package processor;

import model.Sms;
import model.Transaction;

import java.util.Set;

/**
 * @author red
 * @since 0.0.1
 */
public interface MessageProcessor {
    Set<Transaction> processMessage(Sms sms);
}
