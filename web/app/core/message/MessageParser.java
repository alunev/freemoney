package core.message;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author red
 * @since 0.0.1
 */
public interface MessageParser {
    String getSourceString();

    String getDestString();

    BigDecimal getAmount();

    Currency getCurrency();

    String getType();
}
