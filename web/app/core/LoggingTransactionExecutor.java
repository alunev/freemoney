package core;

import model.Transaction;
import play.Logger;

/**
 * @author red
 * @since 0.0.1
 */
public class LoggingTransactionExecutor implements TransactionExecutor {


    @Override
    public void execute(Transaction transaction) {
        Logger.info("Executed: " + transaction);
    }
}
