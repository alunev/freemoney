package model;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author red
 * @since 0.0.1
 */

public class Amount {
    private final BigDecimal value;

    private final Currency currency;

    public Amount(BigDecimal value, Currency currency) {
        this.value = value;
        this.currency = currency;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Currency getCurrency() {
        return currency;
    }
}
