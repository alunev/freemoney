package core.message.parser;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import model.TransactionType;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author red
 * @since 0.0.1
 */

@Getter
@Builder
@ToString
public class ParseResult {
    private TransactionType transactionType;

    private String sourceString;

    private String destString;

    private BigDecimal amount;

    private Currency currency;

    private String payeeString;
}
