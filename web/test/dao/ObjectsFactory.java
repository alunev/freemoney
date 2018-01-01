package dao;

import model.Account;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * @author red
 * @since 0.0.1
 */
public class ObjectsFactory {
    public static Account createDummyAccount() {
        return Account.createAccount(
                "",
                "1111",
                "test RUB account",
                Currency.getInstance("RUB"),
                BigDecimal.valueOf(50.0),
                "from XXXX"
        );
    }

    public static Account createDummyAccountWithOwnerId(String ownerId) {
        return Account.createAccount(
                ownerId,
                "1111",
                "test RUB account",
                Currency.getInstance("RUB"),
                BigDecimal.valueOf(50.0),
                "from XXXX"
        );
    }

    public static Account createDummyAccountWithOwnerIdAndNumber(String ownerId, String number) {
        return Account.createAccount(
                ownerId,
                number,
                "test RUB account",
                Currency.getInstance("RUB"),
                BigDecimal.valueOf(50.0),
                "from XXXX"
        );
    }
}
