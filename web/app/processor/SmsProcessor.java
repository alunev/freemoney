package processor;

import model.Sms;
import model.Transaction;

import java.util.Set;

/**
 * @author red
 * @since 0.0.1
 */
public class SmsProcessor implements MessageProcessor {
    @Override
    public Set<Transaction> processMessage(Sms sms) {
        throw new RuntimeException("");
    }
}
