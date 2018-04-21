package core;

import model.Transaction;

/**
 * @author red
 * @since 0.0.1
 */
public class ThrowingTransactionExecutor implements TransactionExecutor {
    @Override
    public void execute(Transaction transaction) {
        throw new RuntimeException();
    }
}
