package core;

import model.Transaction;


/**
 * @author red
 * @since 0.0.1
 */
public interface TransactionExecutor {

    void execute(Transaction transaction);

}
