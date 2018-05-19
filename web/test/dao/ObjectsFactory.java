package dao;

import common.DateUtils;
import model.Account;
import model.MessagePattern;

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

    public static Account createDummyAccountWithId(String id) {
        return Account.createAccount(
                id,
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

    public static MessagePattern createMessagePattern() {
        return new MessagePattern(
                "01",
                "Покупка\\. Карта \\*(\\d{4}). (\\d+\\.\\d+) RUB. OKEY. Доступно (\\d+\\.\\d+) RUB",
                "bank1",
                DateUtils.now()
        );
    }
}
