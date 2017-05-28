package dao;

import model.Account;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author red
 * @since 0.0.1
 */
public class ObjectsFactory {
    public static Account createDummyAccountWithId(String id) {
        return Account.createAccount(
                id,
                null,
                "1111",
                "test RUB account",
                Currency.getInstance("RUB"),
                BigDecimal.valueOf(50.0),
                "from XXXX"
        );
    }

    public static Account createDummyAccountWithIdAndOwnerId(String id, String ownerId) {
        return Account.createAccount(
                id,
                ownerId,
                "1111",
                "test RUB account",
                Currency.getInstance("RUB"),
                BigDecimal.valueOf(50.0),
                "from XXXX"
        );
    }
}
