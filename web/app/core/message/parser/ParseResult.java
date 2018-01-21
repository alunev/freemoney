package core.message.parser;

import lombok.Builder;
import model.TransactionType;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author red
 * @since 0.0.1
 */

@Builder
public class ParseResult {
    private TransactionType transactionType;

    private String sourceString;

    private String destString;

    private BigDecimal amount;

    private Currency currency;

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public String getSourceString() {
        return sourceString;
    }

    public String getDestString() {
        return destString;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Currency getCurrency() {
        return currency;
    }
}
