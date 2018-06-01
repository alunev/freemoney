package core.message.parser;

import model.MessagePattern;
import model.Sms;
import model.TransactionType;
import play.Logger;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Currency;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.google.common.base.Preconditions.checkNotNull;
import static play.Logger.error;
import static play.Logger.info;

/**
 * @author red
 * @since 0.0.1
 */
public class TinkoffMessageParser implements MessageParser {

    @Override
    public Optional<ParseResult> parse(Sms sms, Collection<MessagePattern> patterns) {
        Matcher matcher = patterns.stream()
                .map(MessagePattern::getRegex)
                .map(Pattern::compile)
                .map(p -> p.matcher(sms.getText()))
                .filter(Matcher::matches)
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Failed to match sms text: " + sms));

        ParseResult.ParseResultBuilder builder = ParseResult.builder();
        builder.transactionType(stringToType(matcher.group(1)));
        builder.sourceString(matcher.group(2));
        builder.amount(new BigDecimal(matcher.group(3)));
        builder.currency(stringToCurrency(matcher.group(4)));

        ParseResult result = builder.build();

        Logger.debug("Result: {}", result);

        return Optional.ofNullable(result);
    }

    private Currency stringToCurrency(String s) {
        try {
            return Currency.getInstance(s);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Unknown currency : " + s, e);
        }
    }

    private TransactionType stringToType(String s) {
        if (s.equals("Покупка")) {
            return TransactionType.EXPENSE;
        }

        return TransactionType.UNKNOWN;
    }
}
