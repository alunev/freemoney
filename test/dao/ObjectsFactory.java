package dao;

import model.Account;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author red
 * @since 0.0.1
 */
class ObjectsFactory {
    static Account createDummyAccountWithId(String id) {
        return Account.createAccount(
                id,
                "1111",
                "test RUB account",
                Currency.getInstance("RUB"),
                BigDecimal.valueOf(50.0),
                "from XXXX"
        );
    }
}
