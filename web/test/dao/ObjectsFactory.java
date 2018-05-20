package dao;

import common.DateUtils;
import core.message.parser.ParseResult;
import model.Account;
import model.MessagePattern;
import model.Sms;
import model.TransactionType;

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

    public static Account createDummyAccountWithIdOwnerId(String id, String ownerId) {
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
                "user01",
                "Покупка\\. Карта \\*(\\d{4}). (\\d+\\.\\d+) RUB. OKEY. Доступно (\\d+\\.\\d+) RUB",
                "bank1",
                DateUtils.now()
        );
    }

    public static ParseResult createParseResult() {
        return ParseResult.builder()
                .transactionType(TransactionType.EXPENSE)
                .sourceString("Карта *2222")
                .destString(null)
                .amount(BigDecimal.TEN)
                .currency(Currency.getInstance("RUB"))
                .build();
    }

    public static Sms sampleTfSms() {
        return new Sms("01",
                "android-01",
                "12345",
                "Покупка. Карта *2222. 3344.5 RUB. OKEY. Доступно 12345.92 RUB",
                DateUtils.now()
        );
    }
}
